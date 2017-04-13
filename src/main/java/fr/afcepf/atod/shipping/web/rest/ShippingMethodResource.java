package fr.afcepf.atod.shipping.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.afcepf.atod.shipping.domain.ShippingMethod;
import fr.afcepf.atod.shipping.repository.ShippingMethodRepository;
import fr.afcepf.atod.shipping.service.dto.ShippingMethodDTO;
import fr.afcepf.atod.shipping.service.mapper.ShippingMethodMapper;
import fr.afcepf.atod.shipping.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ShippingMethod.
 */
@RestController
@RequestMapping("/api")
public class ShippingMethodResource {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodResource.class);

    private static final String ENTITY_NAME = "shippingMethod";
        
    private final ShippingMethodRepository shippingMethodRepository;

    private final ShippingMethodMapper shippingMethodMapper;

    public ShippingMethodResource(ShippingMethodRepository shippingMethodRepository, ShippingMethodMapper shippingMethodMapper) {
        this.shippingMethodRepository = shippingMethodRepository;
        this.shippingMethodMapper = shippingMethodMapper;
    }

    /**
     * POST  /shipping-methods : Create a new shippingMethod.
     *
     * @param shippingMethodDTO the shippingMethodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shippingMethodDTO, or with status 400 (Bad Request) if the shippingMethod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shipping-methods")
    @Timed
    public ResponseEntity<ShippingMethodDTO> createShippingMethod(@RequestBody ShippingMethodDTO shippingMethodDTO) throws URISyntaxException {
        log.debug("REST request to save ShippingMethod : {}", shippingMethodDTO);
        if (shippingMethodDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new shippingMethod cannot already have an ID")).body(null);
        }
        ShippingMethod shippingMethod = shippingMethodMapper.shippingMethodDTOToShippingMethod(shippingMethodDTO);
        shippingMethod = shippingMethodRepository.save(shippingMethod);
        ShippingMethodDTO result = shippingMethodMapper.shippingMethodToShippingMethodDTO(shippingMethod);
        return ResponseEntity.created(new URI("/api/shipping-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shipping-methods : Updates an existing shippingMethod.
     *
     * @param shippingMethodDTO the shippingMethodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shippingMethodDTO,
     * or with status 400 (Bad Request) if the shippingMethodDTO is not valid,
     * or with status 500 (Internal Server Error) if the shippingMethodDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shipping-methods")
    @Timed
    public ResponseEntity<ShippingMethodDTO> updateShippingMethod(@RequestBody ShippingMethodDTO shippingMethodDTO) throws URISyntaxException {
        log.debug("REST request to update ShippingMethod : {}", shippingMethodDTO);
        if (shippingMethodDTO.getId() == null) {
            return createShippingMethod(shippingMethodDTO);
        }
        ShippingMethod shippingMethod = shippingMethodMapper.shippingMethodDTOToShippingMethod(shippingMethodDTO);
        shippingMethod = shippingMethodRepository.save(shippingMethod);
        ShippingMethodDTO result = shippingMethodMapper.shippingMethodToShippingMethodDTO(shippingMethod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shippingMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shipping-methods : get all the shippingMethods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shippingMethods in body
     */
    @GetMapping("/shipping-methods")
    @Timed
    public List<ShippingMethodDTO> getAllShippingMethods() {
        log.debug("REST request to get all ShippingMethods");
        List<ShippingMethod> shippingMethods = shippingMethodRepository.findAll();
        return shippingMethodMapper.shippingMethodsToShippingMethodDTOs(shippingMethods);
    }

    /**
     * GET  /shipping-methods/:id : get the "id" shippingMethod.
     *
     * @param id the id of the shippingMethodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shippingMethodDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shipping-methods/{id}")
    @Timed
    public ResponseEntity<ShippingMethodDTO> getShippingMethod(@PathVariable Long id) {
        log.debug("REST request to get ShippingMethod : {}", id);
        ShippingMethod shippingMethod = shippingMethodRepository.findOne(id);
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.shippingMethodToShippingMethodDTO(shippingMethod);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shippingMethodDTO));
    }

    /**
     * DELETE  /shipping-methods/:id : delete the "id" shippingMethod.
     *
     * @param id the id of the shippingMethodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shipping-methods/{id}")
    @Timed
    public ResponseEntity<Void> deleteShippingMethod(@PathVariable Long id) {
        log.debug("REST request to delete ShippingMethod : {}", id);
        shippingMethodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
