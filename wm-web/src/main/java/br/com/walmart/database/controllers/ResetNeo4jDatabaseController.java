package br.com.walmart.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.core.layers.LayerObject;
import br.com.walmart.database.facade.ResetNeo4jDatabaseFacade;
 
@RestController
@RequestMapping("/core/database/reset/neo4j")
public class ResetNeo4jDatabaseController {

	@Autowired
	ResetNeo4jDatabaseFacade facade;
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LayerObject> reset() {
        
		final FacadeContext response = facade.reset();
		
		if (response.isOk()) {
			return new ResponseEntity<LayerObject>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<LayerObject>(response, HttpStatus.BAD_REQUEST);
		}
		 
    }
 	
}
