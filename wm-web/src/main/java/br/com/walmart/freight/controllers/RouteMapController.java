package br.com.walmart.freight.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.walmart.core.converters.BindingResultHumanErrorConverter;
import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.core.layers.LayerObject;
import br.com.walmart.freight.facades.RouteMapFacade;
import br.com.walmart.freight.models.RouteMap;
import br.com.walmart.freight.presenters.RouteMapPresenter;
 
@RestController
@RequestMapping("/maps")
public class RouteMapController {

	@Autowired
	RouteMapFacade facade;
	@Autowired
	RouteMapPresenter routeMapPresenter;
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LayerObject> create(@Valid @RequestBody RouteMap maps, BindingResult bindingResult) {
        
		if (bindingResult.hasErrors()) {
			routeMapPresenter.addErrors(BindingResultHumanErrorConverter.convert(bindingResult.getAllErrors()));
            return new ResponseEntity<LayerObject>(routeMapPresenter, HttpStatus.BAD_REQUEST);
        }
		
		final FacadeContext response = facade.create(maps);
		
		if (response.isOk()) {
			return new ResponseEntity<LayerObject>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<LayerObject>(response, HttpStatus.BAD_REQUEST);
		}
		 
    }
 	
}
