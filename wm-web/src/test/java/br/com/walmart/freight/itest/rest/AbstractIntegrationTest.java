package br.com.walmart.freight.itest.rest;

import static com.jayway.restassured.RestAssured.given;

import org.junit.After;
import org.junit.Before;

import com.jayway.restassured.response.Response;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.models.RouteMap;

public abstract class AbstractIntegrationTest {

	@Before
	public void resetDatabaseBegfore() {
		doNeo4jReset();
	}
	
//	@After
	public void resetDatabaseAfter() {
		doNeo4jReset();
	}
	
	protected Response doFreightJSONRequest(final String json) {
		return given().contentType("application/json").request().body(json).when()
				.post("wm-logistics/freights");
	}
	
	protected Response doFreightRequest(final Logistic logistic) {
		return given().contentType("application/json").request().body(logistic).when()
				.post("wm-logistics/freights");
	}
	
	protected Response doRouteMapRequest(final RouteMap routeMap) {
		return given().contentType("application/json").request().body(routeMap).when()
				.post("wm-logistics/maps");
	}
	
	protected Response doNeo4jReset() {
		return given().contentType("application/json").request().when()
				.post("wm-logistics/core/database/reset/neo4j");
	}
	
}
