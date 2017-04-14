package fr.afcepf.atod.shipping.repository;

import fr.afcepf.atod.shipping.domain.Command;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Command entity.
 */
@SuppressWarnings("unused")
public interface CommandRepository extends JpaRepository<Command,Long> {
	Optional<Command> findOneById(Long id);
}
