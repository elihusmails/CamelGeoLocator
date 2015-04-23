package com.markwebb.camel.geolocator;

import java.net.URI;
import java.net.URL;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GoogleApiUrlSetupProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(GoogleApiUrlSetupProcessor.class);
	
	public static final String CamelGeoLocatorAddress = "CamelGeoLocatorAddress";
	public static final String CamelGeoLocatorLatLon = "CamelGeoLocatorLatLon";
	public static final String CamelGeoLocatorResultFormat = "CamelGeoLocatorResultFormat";
	
	public static final String CamelGeoLocatorFormatJSON = "json";
	public static final String CamelGeoLocatorFormatXML = "xml";
	
	private static final String CamelGeoLocatorGoogleApiUrl = "https://maps.googleapis.com/maps/api/geocode/";
	private static final String CamelGeoLocatorDefaultFormat = CamelGeoLocatorFormatXML;
	
	@Override
	public void process(Exchange exchange) throws Exception {

		StringBuffer buffer = new StringBuffer();
		
		buffer.append(CamelGeoLocatorGoogleApiUrl);
		
		// This seems ugly....
		if( exchange.getIn().getHeader(CamelGeoLocatorResultFormat, String.class) != null ){
			if( exchange.getIn().getHeader(CamelGeoLocatorResultFormat, String.class).equals(CamelGeoLocatorFormatJSON))
				buffer.append(CamelGeoLocatorFormatJSON);
			else if( exchange.getIn().getHeader(CamelGeoLocatorResultFormat, String.class).equals(CamelGeoLocatorFormatXML))
				buffer.append(CamelGeoLocatorFormatXML);
			else
				buffer.append(CamelGeoLocatorDefaultFormat);
		} else {
			buffer.append(CamelGeoLocatorDefaultFormat);
		}
	
		// This seems ugly too...
		if( exchange.getIn().getHeader(CamelGeoLocatorAddress, String.class) != null ){
			buffer.append("?address=");
			buffer.append(exchange.getIn().getHeader(CamelGeoLocatorAddress, String.class));
		} else if( exchange.getIn().getHeader(CamelGeoLocatorLatLon, String.class) != null ){
			buffer.append("?latlng=");
			buffer.append(exchange.getIn().getHeader(CamelGeoLocatorLatLon, String.class));
		}
		
		URL url = new URL(buffer.toString());
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		url = uri.toURL();
		
		log.info("URL: " + url.toString());
		
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		exchange.getOut().setHeader( Exchange.HTTP_URI, url.toString() );
	}
}
