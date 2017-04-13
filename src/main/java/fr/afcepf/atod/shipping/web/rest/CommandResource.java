package fr.afcepf.atod.shipping.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.afcepf.atod.shipping.domain.Command;

import fr.afcepf.atod.shipping.repository.CommandRepository;
import fr.afcepf.atod.shipping.web.rest.util.HeaderUtil;
import fr.afcepf.atod.shipping.service.dto.CommandDTO;
import fr.afcepf.atod.shipping.service.mapper.CommandMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Command.
 */
@RestController
@RequestMapping("/api")
public class CommandResource {

    private final Logger log = LoggerFactory.getLogger(CommandResource.class);

    private static final String ENTITY_NAME = "command";
        
    private final CommandRepository commandRepository;

    private final CommandMapper commandMapper;

    public CommandResource(CommandRepository commandRepository, CommandMapper commandMapper) {
        this.commandRepository = commandRepository;
        this.commandMapper = commandMapper;
    }

    /**
     * POST  /commands : Create a new command.
     *
     * @param commandDTO the commandDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commandDTO, or with status 400 (Bad Request) if the command has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commands")
    @Timed
    public ResponseEntity<CommandDTO> createCommand(@RequestBody CommandDTO commandDTO) throws URISyntaxException {
        log.debug("REST request to save Command : {}", commandDTO);
        if (commandDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new command cannot already have an ID")).body(null);
        }
        Command command = commandMapper.commandDTOToCommand(commandDTO);
        command = commandRepository.save(command);
        CommandDTO result = commandMapper.commandToCommandDTO(command);
        return ResponseEntity.created(new URI("/api/commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commands : Updates an existing command.
     *
     * @param commandDTO the commandDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commandDTO,
     * or with status 400 (Bad Request) if the commandDTO is not valid,
     * or with status 500 (Internal Server Error) if the commandDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commands")
    @Timed
    public ResponseEntity<CommandDTO> updateCommand(@RequestBody CommandDTO commandDTO) throws URISyntaxException {
        log.debug("REST request to update Command : {}", commandDTO);
        if (commandDTO.getId() == null) {
            return createCommand(commandDTO);
        }
        Command command = commandMapper.commandDTOToCommand(commandDTO);
        command = commandRepository.save(command);
        CommandDTO result = commandMapper.commandToCommandDTO(command);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commandDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commands : get all the commands.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commands in body
     */
    @GetMapping("/commands")
    @Timed
    public List<CommandDTO> getAllCommands() {
        log.debug("REST request to get all Commands");
        List<Command> commands = commandRepository.findAll();
        return commandMapper.commandsToCommandDTOs(commands);
    }

    /**
     * GET  /commands/:id : get the "id" command.
     *
     * @param id the id of the commandDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commandDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commands/{id}")
    @Timed
    public ResponseEntity<CommandDTO> getCommand(@PathVariable Long id) {
        log.debug("REST request to get Command : {}", id);
        Command command = commandRepository.findOne(id);
        CommandDTO commandDTO = commandMapper.commandToCommandDTO(command);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commandDTO));
    }

    /**
     * DELETE  /commands/:id : delete the "id" command.
     *
     * @param id the id of the commandDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commands/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.debug("REST request to delete Command : {}", id);
        commandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
