package fr.afcepf.atod.shipping.repository;

import fr.afcepf.atod.shipping.domain.ShippingMethod;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShippingMethod entity.
 */
@SuppressWarnings("unused")
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod,Long> {

}
