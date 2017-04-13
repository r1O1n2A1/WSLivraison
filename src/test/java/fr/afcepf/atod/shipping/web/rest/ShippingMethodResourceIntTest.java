package fr.afcepf.atod.shipping.web.rest;

import fr.afcepf.atod.shipping.WsLivraisonApp;

import fr.afcepf.atod.shipping.domain.ShippingMethod;
import fr.afcepf.atod.shipping.repository.ShippingMethodRepository;
import fr.afcepf.atod.shipping.service.dto.ShippingMethodDTO;
import fr.afcepf.atod.shipping.service.mapper.ShippingMethodMapper;
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
 * Test class for the ShippingMethodResource REST controller.
 *
 * @see ShippingMethodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsLivraisonApp.class)
public class ShippingMethodResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Autowired
    private ShippingMethodMapper shippingMethodMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShippingMethodMockMvc;

    private ShippingMethod shippingMethod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShippingMethodResource shippingMethodResource = new ShippingMethodResource(shippingMethodRepository, shippingMethodMapper);
        this.restShippingMethodMockMvc = MockMvcBuilders.standaloneSetup(shippingMethodResource)
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
    public static ShippingMethod createEntity(EntityManager em) {
        ShippingMethod shippingMethod = new ShippingMethod()
            .label(DEFAULT_LABEL);
        return shippingMethod;
    }

    @Before
    public void initTest() {
        shippingMethod = createEntity(em);
    }

    @Test
    @Transactional
    public void createShippingMethod() throws Exception {
        int databaseSizeBeforeCreate = shippingMethodRepository.findAll().size();

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.shippingMethodToShippingMethodDTO(shippingMethod);
        restShippingMethodMockMvc.perform(post("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO)))
            .andExpect(status().isCreated());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createShippingMethodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shippingMethodRepository.findAll().size();

        // Create the ShippingMethod with an existing ID
        shippingMethod.setId(1L);
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.shippingMethodToShippingMethodDTO(shippingMethod);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMethodMockMvc.perform(post("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShippingMethods() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList
        restShippingMethodMockMvc.perform(get("/api/shipping-methods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    @Test
    @Transactional
    public void getShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get the shippingMethod
        restShippingMethodMockMvc.perform(get("/api/shipping-methods/{id}", shippingMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shippingMethod.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShippingMethod() throws Exception {
        // Get the shippingMethod
        restShippingMethodMockMvc.perform(get("/api/shipping-methods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Update the shippingMethod
        ShippingMethod updatedShippingMethod = shippingMethodRepository.findOne(shippingMethod.getId());
        updatedShippingMethod
            .label(UPDATED_LABEL);
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.shippingMethodToShippingMethodDTO(updatedShippingMethod);

        restShippingMethodMockMvc.perform(put("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO)))
            .andExpect(status().isOk());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.shippingMethodToShippingMethodDTO(shippingMethod);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShippingMethodMockMvc.perform(put("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO)))
            .andExpect(status().isCreated());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);
        int databaseSizeBeforeDelete = shippingMethodRepository.findAll().size();

        // Get the shippingMethod
        restShippingMethodMockMvc.perform(delete("/api/shipping-methods/{id}", shippingMethod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingMethod.class);
    }
}
