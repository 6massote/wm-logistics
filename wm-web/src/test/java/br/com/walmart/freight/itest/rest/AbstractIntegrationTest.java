package br.com.walmart.freight.itest.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.models.RouteMap;
import io.restassured.response.Response;
import tools.jackson.databind.json.JsonMapper;

/**
 * Boots the whole application against a throw-away Neo4j container (needs Docker;
 * the tests are skipped when Docker is not available).
 */
@Testcontainers(disabledWithoutDocker = true)
@Import(Neo4jContainerConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "wm.database-reset.enabled=true")
public abstract class AbstractIntegrationTest {

	@LocalServerPort
	private int port;
	@Autowired
	private JsonMapper jsonMapper;

	@BeforeEach
	void resetDatabaseBefore() {
		doNeo4jReset();
	}

	private String url(final String path) {
		return "http://localhost:" + port + "/wm-logistics/" + path;
	}

	protected Response doFreightJSONRequest(final String json) {
		return given().contentType("application/json").body(json).when().post(url("freights"));
	}

	protected Response doFreightRequest(final Logistic logistic) {
		return doFreightJSONRequest(jsonMapper.writeValueAsString(logistic));
	}

	protected Response doRouteMapRequest(final RouteMap routeMap) {
		return given().contentType("application/json").body(jsonMapper.writeValueAsString(routeMap)).when()
				.post(url("maps"));
	}

	protected Response doNeo4jReset() {
		return given().contentType("application/json").when().post(url("core/database/reset/neo4j"));
	}

}
