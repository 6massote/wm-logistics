package br.com.walmart.database.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.core.facade.AbstractFacade;
import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.database.repository.Neo4jRepository;

@Service("resetNeo4jDatabaseFacade")
public class ResetNeo4jDatabaseFacadeImpl extends AbstractFacade implements ResetNeo4jDatabaseFacade {

	private static final Logger logger = LoggerFactory.getLogger(ResetNeo4jDatabaseFacadeImpl.class);

	@Autowired
	private Neo4jRepository neo4jRepository;

	@Override
	public FacadeContext reset() {
		final FacadeContext facadeContext = getFacadeContext();

		try {
			neo4jRepository.resetNodesAndRelationships();

			facadeContext.addMessage("Neo4j Database reseted...");
		} catch (RuntimeException e) {
			logger.error("Error resetting the Neo4j database", e);
			facadeContext.addError("error.system.database.reset_failed");
		}

		return facadeContext;
	}

}
