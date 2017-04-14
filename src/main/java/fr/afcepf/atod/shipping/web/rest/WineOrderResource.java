package fr.afcepf.atod.shipping.web.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
	private StringBuilder sb = new StringBuilder();
	public static final String ID_ORDER = "idOrder";
	public static final String API_KEY 
	="AIzaSyAgCLfYg4Q-cEhVhpAIyZ_f9BFQuvhEKsI";
	public  static final int MIN_ID_SECURE = 1000;
	public static final  int MAX_ID_SECURE = 1000000000;


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
		if (incomingInfos.length != 0) {
			saveIncommingCommand(incomingInfos);
		}// save address
		return ResponseEntity.created(new URI(SOAP_WS_SHIPPING))
				.headers(HeaderUtil.createEntityCreationAlert("Batman Return","ok"))
				.body("{description: went to ShippingApp@Jhipster}");
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

	
	private AddressDTO saveIncomingAddress(String[] incomming) {
		Address addres = new Address();
		AddressDTO addressDTO = new AddressDTO();
		
		return addressDTO;
	}
	
	private CommandDTO saveIncommingCommand(String[] incomming){
		Command command = new Command();
		CommandDTO commandDTO = new CommandDTO();
		// parsing to find command
		for (String str: incomming) {
			if(str.contains(ID_ORDER)) {
				String idOrder = str.split(":")[1];
				command.setId(Long.valueOf(idOrder));
				ZonedDateTime zoneDateTime = ZonedDateTime.ofInstant(
						new Date().toInstant(),
						ZoneId.systemDefault());
				command.setDateOrder(zoneDateTime);
				while (commandRepository.findOneById(Long.parseLong(idOrder))
						.isPresent()) {
					logger.error("command already added to the db...");
					SecureRandom random = new SecureRandom();
					idOrder  = Integer.toString(random.nextInt((MAX_ID_SECURE - MIN_ID_SECURE) + 1) 
							+ MIN_ID_SECURE);
				} 
				command = commandRepository.save(command);	
			}
		}
		if (command != null) {
			commandDTO = commandMapper.commandToCommandDTO(command);
		}
		return commandDTO;
	}

	/**
	 * Test if an adress exist
	 * @param address {@link Address}
	 * @return
	 */
	public boolean isExistingAddress(Address address) {
		String request = sb.append("https://maps.googleapis.com/maps/api/geocode/json?address=")
				.append(address.getNum())
				.append("+")
				.append(address.getAddress())
				.append("+")
				.append(address.getZipCode())
				.append("+")
				.append(address.getCountry())
				.append("&key=")
				.append(API_KEY)
				.toString();
		request = request.replace(' ', '+');
		URL url;
		HttpURLConnection httpRQ = null;
		try {
			//Create connection
			url = new URL(request);
			httpRQ = (HttpURLConnection) url.openConnection();
			httpRQ.setRequestMethod("POST");
			httpRQ.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			httpRQ.setRequestProperty("Content-Length", 
					Integer.toString(request.getBytes().length));
			httpRQ.setRequestProperty("Content-Language", "en-US");  

			httpRQ.setUseCaches(false);
			httpRQ.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (
					httpRQ.getOutputStream());
			wr.writeBytes(request);
			wr.close();

			//Get Response  
			InputStream is = httpRQ.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (httpRQ != null) {
				httpRQ.disconnect();
			}
		}
	}

}
