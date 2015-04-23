package com.markwebb.camel.geolocator;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URL;

import org.junit.Test;

public class UrlEncodingTest {

	@Test
	public void test() throws Exception {
		
		String expected = "https://maps.googleapis.com/maps/api/geocode/xml?address=1600%20Amphitheatre%20Parkway,%20Mountain+View,%20CA";
		
		String mapApi = "https://maps.googleapis.com/maps/api/geocode/xml?address=";
		String address = "1600 Amphitheatre Parkway, Mountain+View, CA";
		
		URL url = new URL(mapApi + address);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		url = uri.toURL();
		assertEquals(expected, url.toString());
	}
}
