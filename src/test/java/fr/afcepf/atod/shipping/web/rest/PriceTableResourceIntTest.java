package fr.afcepf.atod.shipping.web.rest;

import fr.afcepf.atod.shipping.WsLivraisonApp;

import fr.afcepf.atod.shipping.domain.PriceTable;
import fr.afcepf.atod.shipping.repository.PriceTableRepository;
import fr.afcepf.atod.shipping.service.dto.PriceTableDTO;
import fr.afcepf.atod.shipping.service.mapper.PriceTableMapper;
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
 * Test class for the PriceTableResource REST controller.
 *
 * @see PriceTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsLivraisonApp.class)
public class PriceTableResourceIntTest {

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Integer DEFAULT_RANGE_LOW = 1;
    private static final Integer UPDATED_RANGE_LOW = 2;

    private static final Integer DEFAULT_RANGE_HIGH = 1;
    private static final Integer UPDATED_RANGE_HIGH = 2;

    @Autowired
    private PriceTableRepository priceTableRepository;

    @Autowired
    private PriceTableMapper priceTableMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPriceTableMockMvc;

    private PriceTable priceTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriceTableResource priceTableResource = new PriceTableResource(priceTableRepository, priceTableMapper);
        this.restPriceTableMockMvc = MockMvcBuilders.standaloneSetup(priceTableResource)
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
    public static PriceTable createEntity(EntityManager em) {
        PriceTable priceTable = new PriceTable()
            .price(DEFAULT_PRICE)
            .rangeLow(DEFAULT_RANGE_LOW)
            .rangeHigh(DEFAULT_RANGE_HIGH);
        return priceTable;
    }

    @Before
    public void initTest() {
        priceTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createPriceTable() throws Exception {
        int databaseSizeBeforeCreate = priceTableRepository.findAll().size();

        // Create the PriceTable
        PriceTableDTO priceTableDTO = priceTableMapper.priceTableToPriceTableDTO(priceTable);
        restPriceTableMockMvc.perform(post("/api/price-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priceTableDTO)))
            .andExpect(status().isCreated());

        // Validate the PriceTable in the database
        List<PriceTable> priceTableList = priceTableRepository.findAll();
        assertThat(priceTableList).hasSize(databaseSizeBeforeCreate + 1);
        PriceTable testPriceTable = priceTableList.get(priceTableList.size() - 1);
        assertThat(testPriceTable.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPriceTable.getRangeLow()).isEqualTo(DEFAULT_RANGE_LOW);
        assertThat(testPriceTable.getRangeHigh()).isEqualTo(DEFAULT_RANGE_HIGH);
    }

    @Test
    @Transactional
    public void createPriceTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = priceTableRepository.findAll().size();

        // Create the PriceTable with an existing ID
        priceTable.setId(1L);
        PriceTableDTO priceTableDTO = priceTableMapper.priceTableToPriceTableDTO(priceTable);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceTableMockMvc.perform(post("/api/price-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priceTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PriceTable> priceTableList = priceTableRepository.findAll();
        assertThat(priceTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPriceTables() throws Exception {
        // Initialize the database
        priceTableRepository.saveAndFlush(priceTable);

        // Get all the priceTableList
        restPriceTableMockMvc.perform(get("/api/price-tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].rangeLow").value(hasItem(DEFAULT_RANGE_LOW)))
            .andExpect(jsonPath("$.[*].rangeHigh").value(hasItem(DEFAULT_RANGE_HIGH)));
    }

    @Test
    @Transactional
    public void getPriceTable() throws Exception {
        // Initialize the database
        priceTableRepository.saveAndFlush(priceTable);

        // Get the priceTable
        restPriceTableMockMvc.perform(get("/api/price-tables/{id}", priceTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(priceTable.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.rangeLow").value(DEFAULT_RANGE_LOW))
            .andExpect(jsonPath("$.rangeHigh").value(DEFAULT_RANGE_HIGH));
    }

    @Test
    @Transactional
    public void getNonExistingPriceTable() throws Exception {
        // Get the priceTable
        restPriceTableMockMvc.perform(get("/api/price-tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriceTable() throws Exception {
        // Initialize the database
        priceTableRepository.saveAndFlush(priceTable);
        int databaseSizeBeforeUpdate = priceTableRepository.findAll().size();

        // Update the priceTable
        PriceTable updatedPriceTable = priceTableRepository.findOne(priceTable.getId());
        updatedPriceTable
            .price(UPDATED_PRICE)
            .rangeLow(UPDATED_RANGE_LOW)
            .rangeHigh(UPDATED_RANGE_HIGH);
        PriceTableDTO priceTableDTO = priceTableMapper.priceTableToPriceTableDTO(updatedPriceTable);

        restPriceTableMockMvc.perform(put("/api/price-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priceTableDTO)))
            .andExpect(status().isOk());

        // Validate the PriceTable in the database
        List<PriceTable> priceTableList = priceTableRepository.findAll();
        assertThat(priceTableList).hasSize(databaseSizeBeforeUpdate);
        PriceTable testPriceTable = priceTableList.get(priceTableList.size() - 1);
        assertThat(testPriceTable.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPriceTable.getRangeLow()).isEqualTo(UPDATED_RANGE_LOW);
        assertThat(testPriceTable.getRangeHigh()).isEqualTo(UPDATED_RANGE_HIGH);
    }

    @Test
    @Transactional
    public void updateNonExistingPriceTable() throws Exception {
        int databaseSizeBeforeUpdate = priceTableRepository.findAll().size();

        // Create the PriceTable
        PriceTableDTO priceTableDTO = priceTableMapper.priceTableToPriceTableDTO(priceTable);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPriceTableMockMvc.perform(put("/api/price-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priceTableDTO)))
            .andExpect(status().isCreated());

        // Validate the PriceTable in the database
        List<PriceTable> priceTableList = priceTableRepository.findAll();
        assertThat(priceTableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePriceTable() throws Exception {
        // Initialize the database
        priceTableRepository.saveAndFlush(priceTable);
        int databaseSizeBeforeDelete = priceTableRepository.findAll().size();

        // Get the priceTable
        restPriceTableMockMvc.perform(delete("/api/price-tables/{id}", priceTable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PriceTable> priceTableList = priceTableRepository.findAll();
        assertThat(priceTableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceTable.class);
    }
}
