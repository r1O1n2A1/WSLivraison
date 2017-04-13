package fr.afcepf.atod.shipping.web.rest;

import fr.afcepf.atod.shipping.WsLivraisonApp;

import fr.afcepf.atod.shipping.domain.Postman;
import fr.afcepf.atod.shipping.repository.PostmanRepository;
import fr.afcepf.atod.shipping.service.dto.PostmanDTO;
import fr.afcepf.atod.shipping.service.mapper.PostmanMapper;
import fr.afcepf.atod.shipping.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PostmanResource REST controller.
 *
 * @see PostmanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsLivraisonApp.class)
public class PostmanResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_MAX_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_MAX_PRICE = "BBBBBBBBBB";

    @Autowired
    private PostmanRepository postmanRepository;

    @Autowired
    private PostmanMapper postmanMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostmanMockMvc;

    private Postman postman;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostmanResource postmanResource = new PostmanResource(postmanRepository, postmanMapper);
        this.restPostmanMockMvc = MockMvcBuilders.standaloneSetup(postmanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Postman createEntity(EntityManager em) {
        Postman postman = new Postman()
            .label(DEFAULT_LABEL)
            .maxPrice(DEFAULT_MAX_PRICE);
        return postman;
    }

    @Before
    public void initTest() {
        postman = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostman() throws Exception {
        int databaseSizeBeforeCreate = postmanRepository.findAll().size();

        // Create the Postman
        PostmanDTO postmanDTO = postmanMapper.postmanToPostmanDTO(postman);
        restPostmanMockMvc.perform(post("/api/postmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postmanDTO)))
            .andExpect(status().isCreated());

        // Validate the Postman in the database
        List<Postman> postmanList = postmanRepository.findAll();
        assertThat(postmanList).hasSize(databaseSizeBeforeCreate + 1);
        Postman testPostman = postmanList.get(postmanList.size() - 1);
        assertThat(testPostman.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testPostman.getMaxPrice()).isEqualTo(DEFAULT_MAX_PRICE);
    }

    @Test
    @Transactional
    public void createPostmanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postmanRepository.findAll().size();

        // Create the Postman with an existing ID
        postman.setId(1L);
        PostmanDTO postmanDTO = postmanMapper.postmanToPostmanDTO(postman);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostmanMockMvc.perform(post("/api/postmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postmanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Postman> postmanList = postmanRepository.findAll();
        assertThat(postmanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPostmen() throws Exception {
        // Initialize the database
        postmanRepository.saveAndFlush(postman);

        // Get all the postmanList
        restPostmanMockMvc.perform(get("/api/postmen?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postman.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].maxPrice").value(hasItem(DEFAULT_MAX_PRICE.toString())));
    }

    @Test
    @Transactional
    public void getPostman() throws Exception {
        // Initialize the database
        postmanRepository.saveAndFlush(postman);

        // Get the postman
        restPostmanMockMvc.perform(get("/api/postmen/{id}", postman.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postman.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.maxPrice").value(DEFAULT_MAX_PRICE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostman() throws Exception {
        // Get the postman
        restPostmanMockMvc.perform(get("/api/postmen/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostman() throws Exception {
        // Initialize the database
        postmanRepository.saveAndFlush(postman);
        int databaseSizeBeforeUpdate = postmanRepository.findAll().size();

        // Update the postman
        Postman updatedPostman = postmanRepository.findOne(postman.getId());
        updatedPostman
            .label(UPDATED_LABEL)
            .maxPrice(UPDATED_MAX_PRICE);
        PostmanDTO postmanDTO = postmanMapper.postmanToPostmanDTO(updatedPostman);

        restPostmanMockMvc.perform(put("/api/postmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postmanDTO)))
            .andExpect(status().isOk());

        // Validate the Postman in the database
        List<Postman> postmanList = postmanRepository.findAll();
        assertThat(postmanList).hasSize(databaseSizeBeforeUpdate);
        Postman testPostman = postmanList.get(postmanList.size() - 1);
        assertThat(testPostman.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPostman.getMaxPrice()).isEqualTo(UPDATED_MAX_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingPostman() throws Exception {
        int databaseSizeBeforeUpdate = postmanRepository.findAll().size();

        // Create the Postman
        PostmanDTO postmanDTO = postmanMapper.postmanToPostmanDTO(postman);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPostmanMockMvc.perform(put("/api/postmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postmanDTO)))
            .andExpect(status().isCreated());

        // Validate the Postman in the database
        List<Postman> postmanList = postmanRepository.findAll();
        assertThat(postmanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePostman() throws Exception {
        // Initialize the database
        postmanRepository.saveAndFlush(postman);
        int databaseSizeBeforeDelete = postmanRepository.findAll().size();

        // Get the postman
        restPostmanMockMvc.perform(delete("/api/postmen/{id}", postman.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Postman> postmanList = postmanRepository.findAll();
        assertThat(postmanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Postman.class);
    }
}
