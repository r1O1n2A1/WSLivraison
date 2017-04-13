package fr.afcepf.atod.shipping.service.mapper;

import fr.afcepf.atod.shipping.domain.*;
import fr.afcepf.atod.shipping.service.dto.PriceTableDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PriceTable and its DTO PriceTableDTO.
 */
@Mapper(componentModel = "spring", uses = {PostmanMapper.class, })
public interface PriceTableMapper {

    @Mapping(source = "postman.id", target = "postmanId")
    PriceTableDTO priceTableToPriceTableDTO(PriceTable priceTable);

    List<PriceTableDTO> priceTablesToPriceTableDTOs(List<PriceTable> priceTables);

    @Mapping(source = "postmanId", target = "postman")
    PriceTable priceTableDTOToPriceTable(PriceTableDTO priceTableDTO);

    List<PriceTable> priceTableDTOsToPriceTables(List<PriceTableDTO> priceTableDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default PriceTable priceTableFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceTable priceTable = new PriceTable();
        priceTable.setId(id);
        return priceTable;
    }
    

}
