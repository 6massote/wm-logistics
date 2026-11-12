package br.com.walmart.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.core.layers.LayerObject;
import br.com.walmart.database.facade.ResetNeo4jDatabaseFacade;

/**
 * Wipes the whole graph, so it only exists when wm.database-reset.enabled=true (dev and tests).
 */
@RestController
@ConditionalOnProperty(name = "wm.database-reset.enabled", havingValue = "true")
@RequestMapping("/core/database/reset/neo4j")
public class ResetNeo4jDatabaseController {

	@Autowired
	ResetNeo4jDatabaseFacade facade;

	@PostMapping
	public ResponseEntity<LayerObject> reset() {
		final FacadeContext response = facade.reset();

		return new ResponseEntity<>(response, response.isOk() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

}
