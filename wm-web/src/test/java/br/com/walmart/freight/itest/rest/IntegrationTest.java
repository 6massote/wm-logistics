package br.com.walmart.freight.itest.rest;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.models.RouteCity;
import br.com.walmart.freight.models.RouteMap;

public class IntegrationTest extends AbstractIntegrationTest {

	@Before
	public void makeNodesAndRelationships() {		
		final Set<RouteCity> routeCities = new HashSet<RouteCity>();
		routeCities.add(new RouteCity("A", "B", 10f));
		routeCities.add(new RouteCity("B", "D", 15f));
		routeCities.add(new RouteCity("A", "C", 20f));
		routeCities.add(new RouteCity("C", "D", 30f));
		routeCities.add(new RouteCity("B", "E", 50f));
		routeCities.add(new RouteCity("D", "E", 30f));
		
		final RouteMap routeMap = new RouteMap("SP", routeCities);
		
		final Response response = doRouteMapRequest(routeMap);
		
		assertEquals(200, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());

		assertEquals(true, jsonPath.get("ok"));
		assertEquals("Maps populated in Graph...", jsonPath.get("messages[0]"));
	}
	
	@Test
	public void callFreightCalculate() {
		final Response response = this.doFreightRequest(new Logistic("SP", "A", "D", 10f, 2.5f));

		assertEquals(200, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());

		assertEquals(true, jsonPath.get("ok"));
		assertEquals(6.25f, jsonPath.getFloat("models[0].amount"), 0);
		assertEquals("Freight calc success...", jsonPath.get("messages[0]"));		
	}
	
	@Test
	public void callFreightCalculateWithJsonRequest() {
		final Response response = this.doFreightJSONRequest("{ \"map\": \"SP\", \"from\": \"A\", \"to\": \"D\", \"autonomy\": \"10\", \"price\": \"2.5\" }");
		
		assertEquals(200, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());

		assertEquals(true, jsonPath.get("ok"));
		assertEquals(6.25f, jsonPath.getFloat("models[0].amount"), 0);
		assertEquals("Freight calc success...", jsonPath.get("messages[0]"));	
	}
	
}
