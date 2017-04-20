package fr.afcepf.atod.shipping.repository;

import fr.afcepf.atod.shipping.domain.Address;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
public interface AddressRepository extends JpaRepository<Address,Long> {
	Optional<Address> findOneById(Long id);
}
