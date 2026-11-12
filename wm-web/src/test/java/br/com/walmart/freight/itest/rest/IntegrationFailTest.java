package br.com.walmart.freight.itest.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.models.RouteCity;
import br.com.walmart.freight.models.RouteMap;

public class IntegrationFailTest extends AbstractIntegrationTest {

	@Test
	void callPopulateRouteMapFail() {		
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
	void callFreightCalculateValidationFail() {
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
