package br.com.walmart.freight.itest.rest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.neo4j.Neo4jContainer;

/**
 * One Neo4j container per Spring test context. The context is cached and shared by every
 * integration test class, so the container must live exactly as long as the context
 * (a static @Container would restart on a new port while the cached context keeps the old one).
 */
@TestConfiguration(proxyBeanMethods = false)
class Neo4jContainerConfig {

	@Bean
	@ServiceConnection
	Neo4jContainer neo4jContainer() {
		return new Neo4jContainer("neo4j:2026.10-community");
	}

}
