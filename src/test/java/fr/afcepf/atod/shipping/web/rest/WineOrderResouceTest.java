package fr.afcepf.atod.shipping.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.afcepf.atod.shipping.WsLivraisonApp;
import fr.afcepf.atod.shipping.repository.AddressRepository;
import fr.afcepf.atod.shipping.repository.CommandRepository;
import fr.afcepf.atod.shipping.service.mapper.AddressMapper;
import fr.afcepf.atod.shipping.service.mapper.CommandMapper;
import fr.afcepf.atod.shipping.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the WineOrderResource REST controller.
 * 
 * @author ronan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsLivraisonApp.class)
public class WineOrderResouceTest {
	private final Logger log = LoggerFactory.getLogger(CommandResource.class);
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	@Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandMapper commandMapper;
    
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressdMapper;

	private MockMvc restMockMvc;

	private static String urlWineEncoded 
		= "XzE6cm9tYW5lJjExMjUwJjJfdG90YWw6MjI1MDFfaWRPcmRlcjoxOTQzOTMyMjBfY3VzdG9tZXI6Um9uYW4mQm9uZCYyJnJ1ZSRkZSRsYSRwYWl4JjIyMjAwJkd1aW5nYW1wJkZyYW5jZQ==";
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		WineOrderResource wineOrder = new WineOrderResource(commandMapper,
				commandRepository, addressdMapper, addressRepository);
		this.restMockMvc = MockMvcBuilders.standaloneSetup(wineOrder)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}
	
	@Test
	@Transactional
	public void testIncomingFromSoapWS() throws Exception {
		restMockMvc.perform(get("/wine/order/{infos}", urlWineEncoded))
			.andExpect(status().is2xxSuccessful());
		
	}
	@Test
	@Transactional
	public void testIncomingWrongFromSoapWS() throws Exception {
		restMockMvc.perform(get("/wine/order/{infos}", "ZW1wdHlVUkw="))
			.andExpect(status().is2xxSuccessful());
		
	}
	
	
}
