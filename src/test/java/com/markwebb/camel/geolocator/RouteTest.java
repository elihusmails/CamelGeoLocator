package com.markwebb.camel.geolocator;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteTest extends CamelTestSupport {
	
	private static final Logger log = LoggerFactory.getLogger(RouteTest.class);
	
    @Test
    public void testRouteXml() throws Exception {

    	Endpoint endpoint = context.getEndpoint("direct:in");
		Exchange exchange = endpoint.createExchange();
		
		exchange.getIn().setHeader(GoogleApiUrlSetupProcessor.CamelGeoLocatorAddress, 
				"1600 Amphitheatre Parkway, Mountain+View, CA");
	
		Exchange out = template.send(endpoint, exchange);
		
		String body = out.getIn().getBody(String.class);
		
		assertTrue( body.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue( body.trim().endsWith("</GeocodeResponse>"));
    }
    
    @Test
    public void testRouteJson() throws Exception {

    	Endpoint endpoint = context.getEndpoint("direct:in");
		Exchange exchange = endpoint.createExchange();
		
		exchange.getIn().setHeader(GoogleApiUrlSetupProcessor.CamelGeoLocatorAddress, 
				"1600 Amphitheatre Parkway, Mountain+View, CA");
		exchange.getIn().setHeader(GoogleApiUrlSetupProcessor.CamelGeoLocatorResultFormat,
				GoogleApiUrlSetupProcessor.CamelGeoLocatorFormatJSON);
		
		Exchange out = template.send(endpoint, exchange);
		
		String body = out.getIn().getBody(String.class);
		
		assertTrue( body.trim().startsWith("{"));
		assertTrue( body.trim().endsWith("}"));
    }
    
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
            	from("direct:in")
            		.process(new GoogleApiUrlSetupProcessor())
            		.to("http4://whatever")
            		.marshal().string()
                	.to("mock:results");
            }
        };
    }
}
