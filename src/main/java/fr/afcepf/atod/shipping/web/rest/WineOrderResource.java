package fr.afcepf.atod.shipping.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.afcepf.atod.shipping.domain.Address;
import fr.afcepf.atod.shipping.domain.Command;
import fr.afcepf.atod.shipping.repository.AddressRepository;
import fr.afcepf.atod.shipping.repository.CommandRepository;
import fr.afcepf.atod.shipping.service.dto.AddressDTO;
import fr.afcepf.atod.shipping.service.dto.CommandDTO;
import fr.afcepf.atod.shipping.service.mapper.AddressMapper;
import fr.afcepf.atod.shipping.service.mapper.CommandMapper;
import fr.afcepf.atod.shipping.service.util.ConstantsUtiles;
import fr.afcepf.atod.shipping.service.util.GoogleMapAPI;
import fr.afcepf.atod.shipping.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/wine")
public class WineOrderResource {
	private final Logger logger = 
			LoggerFactory.getLogger(WineOrderResource.class);
	public static final String SOAP_WS_SHIPPING = "http://localhost:28080/SOAPShipping/";

	private final CommandMapper commandMapper;
	private final CommandRepository commandRepository;
	private final AddressMapper addressMapper;
	private final AddressRepository addressRepository;		

	public WineOrderResource(CommandMapper commandMapper, 
			CommandRepository commandRepository,
			AddressMapper addressMapper, AddressRepository adressRepository) {
		this.commandMapper = commandMapper;
		this.commandRepository = commandRepository;
		this.addressMapper = addressMapper;
		this.addressRepository = adressRepository;
	}

	@GetMapping("/order/{infos}")
	@Timed
	public ResponseEntity<?> createOrderFomWine(@PathVariable String infos)
			throws URISyntaxException, UnsupportedEncodingException {
		logger.info("REST request from Wine-App");
		// parsing info
		String[] incomingInfos = saveInfoComingSoapWS(decodeIncomingUrl(infos));
		incomingInfos = ArrayUtils.removeElement(incomingInfos, "");
		// save command
		CommandDTO commandDTO = null;
		if (incomingInfos.length != 0) {
			commandDTO = saveCommandWithAdress(incomingInfos);
		}

		if (commandDTO.getId() != null) {
			return ResponseEntity.created(new URI(SOAP_WS_SHIPPING))
					.headers(HeaderUtil.createEntityCreationAlert(ConstantsUtiles.CMD_ADDRESS, "validated"))
					.body("description:command correctly created, id: " + commandDTO.getId());
		} else {
			return ResponseEntity.created(new URI(SOAP_WS_SHIPPING))
					.headers(HeaderUtil.createEntityCreationAlert(ConstantsUtiles.CMD_ADDRESS, "not-validated"))
					.body("{description:command could not be created}");
		}
	}

	@GetMapping("/order/shipping/{idShipping}")
	@Timed
	public ResponseEntity<?> getShippingFromApp(@PathVariable String idShipping) 
			throws URISyntaxException, UnsupportedEncodingException {
		logger.info("REST test isShipping from Wine-App");
		CommandDTO commandDTO = null;
		if (!idShipping.equalsIgnoreCase(ConstantsUtiles.EMPTY_STR)
				&& commandRepository.findOneById(
						Long.valueOf(decodeIncomingUrl(idShipping))).isPresent()) {
			Command command = commandRepository.findOneById(
					Long.valueOf(decodeIncomingUrl(idShipping))).get();
			if (command != null) {
				commandDTO = commandMapper.commandToCommandDTO(command);
			}
		}
		if (commandDTO.getId() != null) {
			return ResponseEntity.created(new URI(SOAP_WS_SHIPPING))
					.headers(HeaderUtil.createEntityCreationAlert(ConstantsUtiles.CMD_ADDRESS, 
							commandDTO.getAddressId()  + ConstantsUtiles.FOUND))
					.body("description:command correctly created, address" 
							+ commandDTO.getAddressId()  
							+ ConstantsUtiles.FOUND);
		} else {
			return ResponseEntity.created(new URI(SOAP_WS_SHIPPING))
					.headers(HeaderUtil.createEntityCreationAlert(ConstantsUtiles.CMD_ADDRESS, "address not validated"))
					.body("{description:command not created}");
		}
	}

	/**
	 * Parsing incoming url to get infos
	 * @param infos
	 * @return
	 */
	private String[] saveInfoComingSoapWS(String infos) {
		return infos.replace("&", "|").replace("$", " ")
				.split("_");
	}

	/**
	 * decode incoming URL
	 * @param infos
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private String decodeIncomingUrl(String infos) throws UnsupportedEncodingException {
		byte[] decoded = Base64.getDecoder().decode(infos);
		return new String(decoded, "UTF-8"); 
	}


	/**
	 * @param incomming array string including
	 * all parameters from the order
	 * @return
	 */
	private CommandDTO saveCommandWithAdress(String[] incomming) {
		Command command = new Command();
		CommandDTO commandDTO = new CommandDTO();
		Address address = new Address();
		AddressDTO addressDTO = new AddressDTO();
		String idOrder = ConstantsUtiles.EMPTY_STR;
		// parsing to find command
		for (String str: incomming) {
			if(str.contains(ConstantsUtiles.ID_ORDER)) {
				idOrder = str.split(":")[1];
				command.setId(Long.valueOf(idOrder));
				ZonedDateTime zoneDateTime = ZonedDateTime.ofInstant(
						new Date().toInstant(),
						ZoneId.systemDefault());
				command.setDateOrder(zoneDateTime);

			} else if (str.contains(ConstantsUtiles.CUSTOMER)) {
				String[] customer = str.split(":");
				String temp = customer[1];
				String[] arrayAdress = temp.split(ConstantsUtiles.PARSE_CHARAC);
				setterAddress(arrayAdress, address);
			}			
		}
		if (idOrder != ConstantsUtiles.EMPTY_STR &&
				commandRepository.findOneById(Long.parseLong(idOrder)).isPresent()) {
			while (commandRepository.findOneById(Long.parseLong(idOrder))
					.isPresent()) {
				logger.error("command already added to the db...");
				SecureRandom random = new SecureRandom();
				idOrder  = Integer.toString(random.nextInt(
						(ConstantsUtiles.MAX_ID_SECURE - ConstantsUtiles.MIN_ID_SECURE) + 1) 
						+ ConstantsUtiles.MIN_ID_SECURE);
			} 
			boolean isAdress = GoogleMapAPI.isExistingAddress(address);
			if (isAdress) {
				command.setAddress(address);
			} 
			addressRepository.save(address);
			command = commandRepository.save(command);
		}

		if (command != null) {
			commandDTO = commandMapper.commandToCommandDTO(command);
		}
		return commandDTO;
	}

	/**
	 * set address properties from incoming url
	 * @param arrayAdress
	 * @param address
	 * @return
	 */
	private Address setterAddress(String[] arrayAdress, Address address) {
		if (arrayAdress.length != 0) {
			address.setNum(arrayAdress[2]);
			address.setAddress(arrayAdress[3]);
			address.setCity(arrayAdress[4]);
			address.setCountry(arrayAdress[5]);
		}
		return address;
	}
}
