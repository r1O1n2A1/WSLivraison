package fr.afcepf.atod.shipping.service.mapper;

import fr.afcepf.atod.shipping.domain.*;
import fr.afcepf.atod.shipping.service.dto.PostmanDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Postman and its DTO PostmanDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostmanMapper {

    PostmanDTO postmanToPostmanDTO(Postman postman);

    List<PostmanDTO> postmenToPostmanDTOs(List<Postman> postmen);

    @Mapping(target = "commands", ignore = true)
    @Mapping(target = "priceTables", ignore = true)
    @Mapping(target = "regions", ignore = true)
    Postman postmanDTOToPostman(PostmanDTO postmanDTO);

    List<Postman> postmanDTOsToPostmen(List<PostmanDTO> postmanDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Postman postmanFromId(Long id) {
        if (id == null) {
            return null;
        }
        Postman postman = new Postman();
        postman.setId(id);
        return postman;
    }
    

}
