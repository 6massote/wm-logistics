package br.com.walmart.database.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.core.facade.AbstractFacade;
import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.database.repository.Neo4jRepository;

@Service("resetNeo4jDatabaseFacade")
public class ResetNeo4jDatabaseFacadeImpl extends AbstractFacade implements ResetNeo4jDatabaseFacade {

	@Autowired
	private Neo4jRepository neo4jRepository;
	
	public FacadeContext reset() {
		final FacadeContext facadeContext = getFacadeContext();
		
		try {
			neo4jRepository.resetNodesAndRelationships();
			
			facadeContext.addMessage("Neo4j Database reseted...");
		} catch (Exception e) {
			facadeContext.addError(e.getMessage());
			e.printStackTrace();
		}
		
		return facadeContext;
	}
	
}
