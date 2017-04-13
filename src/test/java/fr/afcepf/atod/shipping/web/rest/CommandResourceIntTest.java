package fr.afcepf.atod.shipping.web.rest;

import fr.afcepf.atod.shipping.WsLivraisonApp;

import fr.afcepf.atod.shipping.domain.Command;
import fr.afcepf.atod.shipping.repository.CommandRepository;
import fr.afcepf.atod.shipping.service.dto.CommandDTO;
import fr.afcepf.atod.shipping.service.mapper.CommandMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static fr.afcepf.atod.shipping.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommandResource REST controller.
 *
 * @see CommandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsLivraisonApp.class)
public class CommandResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_ORDER = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ORDER = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_TAKEN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TAKEN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_SHIPPING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_SHIPPING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_DELIVERY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DELIVERY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandMapper commandMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommandMockMvc;

    private Command command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandResource commandResource = new CommandResource(commandRepository, commandMapper);
        this.restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
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
    public static Command createEntity(EntityManager em) {
        Command command = new Command()
            .dateOrder(DEFAULT_DATE_ORDER)
            .dateTaken(DEFAULT_DATE_TAKEN)
            .dateShipping(DEFAULT_DATE_SHIPPING)
            .dateDelivery(DEFAULT_DATE_DELIVERY);
        return command;
    }

    @Before
    public void initTest() {
        command = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command
        CommandDTO commandDTO = commandMapper.commandToCommandDTO(command);
        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getDateOrder()).isEqualTo(DEFAULT_DATE_ORDER);
        assertThat(testCommand.getDateTaken()).isEqualTo(DEFAULT_DATE_TAKEN);
        assertThat(testCommand.getDateShipping()).isEqualTo(DEFAULT_DATE_SHIPPING);
        assertThat(testCommand.getDateDelivery()).isEqualTo(DEFAULT_DATE_DELIVERY);
    }

    @Test
    @Transactional
    public void createCommandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command with an existing ID
        command.setId(1L);
        CommandDTO commandDTO = commandMapper.commandToCommandDTO(command);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOrder").value(hasItem(sameInstant(DEFAULT_DATE_ORDER))))
            .andExpect(jsonPath("$.[*].dateTaken").value(hasItem(sameInstant(DEFAULT_DATE_TAKEN))))
            .andExpect(jsonPath("$.[*].dateShipping").value(hasItem(sameInstant(DEFAULT_DATE_SHIPPING))))
            .andExpect(jsonPath("$.[*].dateDelivery").value(hasItem(sameInstant(DEFAULT_DATE_DELIVERY))));
    }

    @Test
    @Transactional
    public void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.dateOrder").value(sameInstant(DEFAULT_DATE_ORDER)))
            .andExpect(jsonPath("$.dateTaken").value(sameInstant(DEFAULT_DATE_TAKEN)))
            .andExpect(jsonPath("$.dateShipping").value(sameInstant(DEFAULT_DATE_SHIPPING)))
            .andExpect(jsonPath("$.dateDelivery").value(sameInstant(DEFAULT_DATE_DELIVERY)));
    }

    @Test
    @Transactional
    public void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        Command updatedCommand = commandRepository.findOne(command.getId());
        updatedCommand
            .dateOrder(UPDATED_DATE_ORDER)
            .dateTaken(UPDATED_DATE_TAKEN)
            .dateShipping(UPDATED_DATE_SHIPPING)
            .dateDelivery(UPDATED_DATE_DELIVERY);
        CommandDTO commandDTO = commandMapper.commandToCommandDTO(updatedCommand);

        restCommandMockMvc.perform(put("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getDateOrder()).isEqualTo(UPDATED_DATE_ORDER);
        assertThat(testCommand.getDateTaken()).isEqualTo(UPDATED_DATE_TAKEN);
        assertThat(testCommand.getDateShipping()).isEqualTo(UPDATED_DATE_SHIPPING);
        assertThat(testCommand.getDateDelivery()).isEqualTo(UPDATED_DATE_DELIVERY);
    }

    @Test
    @Transactional
    public void updateNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Create the Command
        CommandDTO commandDTO = commandMapper.commandToCommandDTO(command);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommandMockMvc.perform(put("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Get the command
        restCommandMockMvc.perform(delete("/api/commands/{id}", command.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Command.class);
    }
}
