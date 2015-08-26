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
		final Set<RouteCity> routeCitiesSP = new HashSet<RouteCity>();
		routeCitiesSP.add(new RouteCity("A", "B", 10f));
		routeCitiesSP.add(new RouteCity("B", "D", 15.5f));
		routeCitiesSP.add(new RouteCity("A", "C", 20f));
		routeCitiesSP.add(new RouteCity("C", "D", 30f));
		routeCitiesSP.add(new RouteCity("B", "E", 50.5f));
		routeCitiesSP.add(new RouteCity("D", "E", 30f));
		routeCitiesSP.add(new RouteCity("F", "G", 20f));
		routeCitiesSP.add(new RouteCity("D", "A", 30.5f));
		
		final RouteMap routeMapSP = new RouteMap("SP", routeCitiesSP);
		final Response responseSP = doRouteMapRequest(routeMapSP);
		
		assertEquals(200, responseSP.getStatusCode());

		final JsonPath jsonPathSP = new JsonPath(responseSP.asString());

		assertEquals(true, jsonPathSP.get("ok"));
		assertEquals("Maps populated in Graph...", jsonPathSP.get("messages[0]"));
		
		final Set<RouteCity> routeCitiesBH = new HashSet<RouteCity>();
		
		routeCitiesBH.add(new RouteCity("Q", "V", 40f));
		routeCitiesBH.add(new RouteCity("H", "Z", 30f));
		routeCitiesBH.add(new RouteCity("D", "G", 20f));
		routeCitiesBH.add(new RouteCity("G", "S", 60f));
		routeCitiesBH.add(new RouteCity("R", "E", 20f));
		routeCitiesBH.add(new RouteCity("X", "H", 10f));
		routeCitiesBH.add(new RouteCity("Y", "T", 10f));
		routeCitiesBH.add(new RouteCity("D", "E", 20f));

		final JsonPath jsonPathBH = new JsonPath(responseSP.asString());

		assertEquals(true, jsonPathBH.get("ok"));
		assertEquals("Maps populated in Graph...", jsonPathBH.get("messages[0]"));
	}
	
	@Test
	public void callFreightCalculate() {
		final Response response = this.doFreightRequest(new Logistic("SP", "A", "D", 10f, 2.5f));

		assertEquals(200, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());

		assertEquals(true, jsonPath.get("ok"));
		assertEquals(6.375f, jsonPath.getFloat("models[0].amount"), 0);
		assertEquals("Freight calc success...", jsonPath.get("messages[0]"));		
	}
	
	@Test
	public void callFreightCalculateWithJsonRequest() {
		final Response response = this.doFreightJSONRequest("{ \"map\": \"SP\", \"from\": \"A\", \"to\": \"D\", \"autonomy\": \"10\", \"price\": \"2.5\" }");
		
		assertEquals(200, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());

		assertEquals(true, jsonPath.get("ok"));
		assertEquals(6.375f, jsonPath.getFloat("models[0].amount"), 0);
		assertEquals("Freight calc success...", jsonPath.get("messages[0]"));	
	}
	
}
