package br.com.walmart.freight.itest.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.models.RouteCity;
import br.com.walmart.freight.models.RouteMap;

public class IntegrationTestFail extends AbstractIntegrationTest {

	@Test
	public void callPopulateRouteMapFail() {		
		final Set<RouteCity> routeCities = new HashSet<RouteCity>();
		final RouteMap routeMap = new RouteMap("", routeCities);
		
		final Response response = doRouteMapRequest(routeMap);
		
		assertEquals(400, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());
		final List<String> errorsList = jsonPath.getList("errors");
		
		assertFalse(jsonPath.getBoolean("ok"));
		assertFalse(errorsList.isEmpty());
		
		assertTrue(errorsList.contains("error.route_map.name.not_empty"));
		assertTrue(errorsList.contains("error.route_map.routes.not_empty"));
	}
	
	@Test
	public void callFreightCalculateValidationFail() {
		final Response response = this.doFreightRequest(new Logistic("", "", "", null, null));
		
		assertEquals(400, response.getStatusCode());

		final JsonPath jsonPath = new JsonPath(response.asString());
		final List<String> errorsList = jsonPath.getList("errors");
		
		assertFalse(jsonPath.getBoolean("ok"));
		assertFalse(errorsList.isEmpty());
		
		assertTrue(errorsList.contains("error.logistic.map.not_empty"));
		assertTrue(errorsList.contains("error.logistic.from.not_empty"));
		assertTrue(errorsList.contains("error.logistic.to.not_empty"));
		assertTrue(errorsList.contains("error.logistic.autonomy.not_null"));
		assertTrue(errorsList.contains("error.logistic.price.not_null"));
	}
	
}
