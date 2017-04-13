package fr.afcepf.atod.shipping.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.afcepf.atod.shipping.domain.Postman;

import fr.afcepf.atod.shipping.repository.PostmanRepository;
import fr.afcepf.atod.shipping.web.rest.util.HeaderUtil;
import fr.afcepf.atod.shipping.service.dto.PostmanDTO;
import fr.afcepf.atod.shipping.service.mapper.PostmanMapper;
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
 * REST controller for managing Postman.
 */
@RestController
@RequestMapping("/api")
public class PostmanResource {

    private final Logger log = LoggerFactory.getLogger(PostmanResource.class);

    private static final String ENTITY_NAME = "postman";
        
    private final PostmanRepository postmanRepository;

    private final PostmanMapper postmanMapper;

    public PostmanResource(PostmanRepository postmanRepository, PostmanMapper postmanMapper) {
        this.postmanRepository = postmanRepository;
        this.postmanMapper = postmanMapper;
    }

    /**
     * POST  /postmen : Create a new postman.
     *
     * @param postmanDTO the postmanDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postmanDTO, or with status 400 (Bad Request) if the postman has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/postmen")
    @Timed
    public ResponseEntity<PostmanDTO> createPostman(@RequestBody PostmanDTO postmanDTO) throws URISyntaxException {
        log.debug("REST request to save Postman : {}", postmanDTO);
        if (postmanDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new postman cannot already have an ID")).body(null);
        }
        Postman postman = postmanMapper.postmanDTOToPostman(postmanDTO);
        postman = postmanRepository.save(postman);
        PostmanDTO result = postmanMapper.postmanToPostmanDTO(postman);
        return ResponseEntity.created(new URI("/api/postmen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /postmen : Updates an existing postman.
     *
     * @param postmanDTO the postmanDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postmanDTO,
     * or with status 400 (Bad Request) if the postmanDTO is not valid,
     * or with status 500 (Internal Server Error) if the postmanDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/postmen")
    @Timed
    public ResponseEntity<PostmanDTO> updatePostman(@RequestBody PostmanDTO postmanDTO) throws URISyntaxException {
        log.debug("REST request to update Postman : {}", postmanDTO);
        if (postmanDTO.getId() == null) {
            return createPostman(postmanDTO);
        }
        Postman postman = postmanMapper.postmanDTOToPostman(postmanDTO);
        postman = postmanRepository.save(postman);
        PostmanDTO result = postmanMapper.postmanToPostmanDTO(postman);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postmanDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /postmen : get all the postmen.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of postmen in body
     */
    @GetMapping("/postmen")
    @Timed
    public List<PostmanDTO> getAllPostmen() {
        log.debug("REST request to get all Postmen");
        List<Postman> postmen = postmanRepository.findAll();
        return postmanMapper.postmenToPostmanDTOs(postmen);
    }

    /**
     * GET  /postmen/:id : get the "id" postman.
     *
     * @param id the id of the postmanDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postmanDTO, or with status 404 (Not Found)
     */
    @GetMapping("/postmen/{id}")
    @Timed
    public ResponseEntity<PostmanDTO> getPostman(@PathVariable Long id) {
        log.debug("REST request to get Postman : {}", id);
        Postman postman = postmanRepository.findOne(id);
        PostmanDTO postmanDTO = postmanMapper.postmanToPostmanDTO(postman);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postmanDTO));
    }

    /**
     * DELETE  /postmen/:id : delete the "id" postman.
     *
     * @param id the id of the postmanDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/postmen/{id}")
    @Timed
    public ResponseEntity<Void> deletePostman(@PathVariable Long id) {
        log.debug("REST request to delete Postman : {}", id);
        postmanRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
