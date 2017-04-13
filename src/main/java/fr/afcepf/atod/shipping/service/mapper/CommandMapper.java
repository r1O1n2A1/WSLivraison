package fr.afcepf.atod.shipping.service.mapper;

import fr.afcepf.atod.shipping.domain.*;
import fr.afcepf.atod.shipping.service.dto.CommandDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Command and its DTO CommandDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, PostmanMapper.class, ShippingMethodMapper.class, })
public interface CommandMapper {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "postman.id", target = "postmanId")
    @Mapping(source = "shippingMethod.id", target = "shippingMethodId")
    CommandDTO commandToCommandDTO(Command command);

    List<CommandDTO> commandsToCommandDTOs(List<Command> commands);

    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "postmanId", target = "postman")
    @Mapping(source = "shippingMethodId", target = "shippingMethod")
    Command commandDTOToCommand(CommandDTO commandDTO);

    List<Command> commandDTOsToCommands(List<CommandDTO> commandDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Command commandFromId(Long id) {
        if (id == null) {
            return null;
        }
        Command command = new Command();
        command.setId(id);
        return command;
    }
    

}
