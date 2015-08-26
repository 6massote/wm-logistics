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
import br.com.walmart.freight.facades.FreightFacade;
import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.presenters.FreightPresenter;

@RestController
@RequestMapping("/freights")
public class FreightController {
	
	@Autowired
	private FreightFacade facade;
	@Autowired
	private FreightPresenter freightPresenter;	
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LayerObject> calculate(@Valid @RequestBody Logistic logistic, BindingResult bindingResult) {
        
		if (bindingResult.hasErrors()) {
			freightPresenter.addErrors(BindingResultHumanErrorConverter.convert(bindingResult.getAllErrors()));
            return new ResponseEntity<LayerObject>(freightPresenter, HttpStatus.BAD_REQUEST);
        }
		
		final FacadeContext response = facade.calculate(logistic);
		
		if (response.isOk()) {
			return new ResponseEntity<LayerObject>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<LayerObject>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
}
