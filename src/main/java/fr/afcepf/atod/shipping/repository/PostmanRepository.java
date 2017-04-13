package fr.afcepf.atod.shipping.repository;

import fr.afcepf.atod.shipping.domain.Postman;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Postman entity.
 */
@SuppressWarnings("unused")
public interface PostmanRepository extends JpaRepository<Postman,Long> {

}
