package fr.afcepf.atod.shipping.service.mapper;

import fr.afcepf.atod.shipping.domain.*;
import fr.afcepf.atod.shipping.service.dto.ShippingMethodDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ShippingMethod and its DTO ShippingMethodDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShippingMethodMapper {

    ShippingMethodDTO shippingMethodToShippingMethodDTO(ShippingMethod shippingMethod);

    List<ShippingMethodDTO> shippingMethodsToShippingMethodDTOs(List<ShippingMethod> shippingMethods);

    @Mapping(target = "commands", ignore = true)
    ShippingMethod shippingMethodDTOToShippingMethod(ShippingMethodDTO shippingMethodDTO);

    List<ShippingMethod> shippingMethodDTOsToShippingMethods(List<ShippingMethodDTO> shippingMethodDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default ShippingMethod shippingMethodFromId(Long id) {
        if (id == null) {
            return null;
        }
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setId(id);
        return shippingMethod;
    }
    

}
