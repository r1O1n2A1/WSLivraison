package fr.afcepf.atod.shipping.repository;

import fr.afcepf.atod.shipping.domain.PriceTable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PriceTable entity.
 */
@SuppressWarnings("unused")
public interface PriceTableRepository extends JpaRepository<PriceTable,Long> {

}
