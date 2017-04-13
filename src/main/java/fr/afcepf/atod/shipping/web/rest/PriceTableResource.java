package fr.afcepf.atod.shipping.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.afcepf.atod.shipping.domain.PriceTable;

import fr.afcepf.atod.shipping.repository.PriceTableRepository;
import fr.afcepf.atod.shipping.web.rest.util.HeaderUtil;
import fr.afcepf.atod.shipping.service.dto.PriceTableDTO;
import fr.afcepf.atod.shipping.service.mapper.PriceTableMapper;
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
 * REST controller for managing PriceTable.
 */
@RestController
@RequestMapping("/api")
public class PriceTableResource {

    private final Logger log = LoggerFactory.getLogger(PriceTableResource.class);

    private static final String ENTITY_NAME = "priceTable";
        
    private final PriceTableRepository priceTableRepository;

    private final PriceTableMapper priceTableMapper;

    public PriceTableResource(PriceTableRepository priceTableRepository, PriceTableMapper priceTableMapper) {
        this.priceTableRepository = priceTableRepository;
        this.priceTableMapper = priceTableMapper;
    }

    /**
     * POST  /price-tables : Create a new priceTable.
     *
     * @param priceTableDTO the priceTableDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priceTableDTO, or with status 400 (Bad Request) if the priceTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/price-tables")
    @Timed
    public ResponseEntity<PriceTableDTO> createPriceTable(@RequestBody PriceTableDTO priceTableDTO) throws URISyntaxException {
        log.debug("REST request to save PriceTable : {}", priceTableDTO);
        if (priceTableDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new priceTable cannot already have an ID")).body(null);
        }
        PriceTable priceTable = priceTableMapper.priceTableDTOToPriceTable(priceTableDTO);
        priceTable = priceTableRepository.save(priceTable);
        PriceTableDTO result = priceTableMapper.priceTableToPriceTableDTO(priceTable);
        return ResponseEntity.created(new URI("/api/price-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /price-tables : Updates an existing priceTable.
     *
     * @param priceTableDTO the priceTableDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priceTableDTO,
     * or with status 400 (Bad Request) if the priceTableDTO is not valid,
     * or with status 500 (Internal Server Error) if the priceTableDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/price-tables")
    @Timed
    public ResponseEntity<PriceTableDTO> updatePriceTable(@RequestBody PriceTableDTO priceTableDTO) throws URISyntaxException {
        log.debug("REST request to update PriceTable : {}", priceTableDTO);
        if (priceTableDTO.getId() == null) {
            return createPriceTable(priceTableDTO);
        }
        PriceTable priceTable = priceTableMapper.priceTableDTOToPriceTable(priceTableDTO);
        priceTable = priceTableRepository.save(priceTable);
        PriceTableDTO result = priceTableMapper.priceTableToPriceTableDTO(priceTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, priceTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /price-tables : get all the priceTables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of priceTables in body
     */
    @GetMapping("/price-tables")
    @Timed
    public List<PriceTableDTO> getAllPriceTables() {
        log.debug("REST request to get all PriceTables");
        List<PriceTable> priceTables = priceTableRepository.findAll();
        return priceTableMapper.priceTablesToPriceTableDTOs(priceTables);
    }

    /**
     * GET  /price-tables/:id : get the "id" priceTable.
     *
     * @param id the id of the priceTableDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priceTableDTO, or with status 404 (Not Found)
     */
    @GetMapping("/price-tables/{id}")
    @Timed
    public ResponseEntity<PriceTableDTO> getPriceTable(@PathVariable Long id) {
        log.debug("REST request to get PriceTable : {}", id);
        PriceTable priceTable = priceTableRepository.findOne(id);
        PriceTableDTO priceTableDTO = priceTableMapper.priceTableToPriceTableDTO(priceTable);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(priceTableDTO));
    }

    /**
     * DELETE  /price-tables/:id : delete the "id" priceTable.
     *
     * @param id the id of the priceTableDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/price-tables/{id}")
    @Timed
    public ResponseEntity<Void> deletePriceTable(@PathVariable Long id) {
        log.debug("REST request to delete PriceTable : {}", id);
        priceTableRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
