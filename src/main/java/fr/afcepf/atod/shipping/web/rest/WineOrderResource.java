package fr.afcepf.atod.shipping.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.afcepf.atod.shipping.repository.CommandRepository;
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
	
	public WineOrderResource(CommandMapper mapper, CommandRepository repository) {
		this.commandMapper = mapper;
		this.commandRepository = repository;
	}
	
	@GetMapping("/order/{order}")
	@Timed
	public ResponseEntity<?> createOrderFomWine(@PathVariable String order) throws URISyntaxException {
		logger.info("REST request from Wine-App");
		
		return ResponseEntity.created(new URI(SOAP_WS_SHIPPING))
				.headers(HeaderUtil.createEntityCreationAlert("Batman Return","ok"))
				.body("{description: went to ShippingApp@Jhipster}");
	}
	
//	private 
}
