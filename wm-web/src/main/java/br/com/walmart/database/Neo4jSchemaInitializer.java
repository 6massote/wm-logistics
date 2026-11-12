package br.com.walmart.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.walmart.database.repository.Neo4jRepository;

/**
 * Makes sure the lookup index used by every route query exists. Idempotent (IF NOT EXISTS).
 */
@Component
public class Neo4jSchemaInitializer implements ApplicationRunner {

	@Autowired
	private Neo4jRepository neo4jRepository;

	@Override
	public void run(ApplicationArguments args) {
		neo4jRepository.createIndexes();
	}

}
