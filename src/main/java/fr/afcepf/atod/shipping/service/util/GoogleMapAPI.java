package fr.afcepf.atod.shipping.service.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.afcepf.atod.shipping.domain.Address;
import fr.afcepf.atod.shipping.web.rest.WineOrderResource;

public final class GoogleMapAPI {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(WineOrderResource.class);
	public static final String API_KEY 
		="AIzaSyAgCLfYg4Q-cEhVhpAIyZ_f9BFQuvhEKsI";
	private static StringBuilder sb = new StringBuilder();
	
	private GoogleMapAPI() {
		// empty
	}	
	
	/**
	 * Test if an adress exist
	 * @param address {@link Address}
	 * @return
	 */
	public static boolean isExistingAddress(Address address) {
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
