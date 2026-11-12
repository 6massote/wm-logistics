package br.com.walmart.freight.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	// Prototype scoped: a fresh presenter per request, otherwise errors would pile up across requests.
	@Autowired
	private ObjectProvider<FreightPresenter> freightPresenters;

	@PostMapping
	public ResponseEntity<LayerObject> calculate(@Valid @RequestBody Logistic logistic, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final FreightPresenter presenter = freightPresenters.getObject();
			presenter.addErrors(BindingResultHumanErrorConverter.convert(bindingResult.getAllErrors()));
			return new ResponseEntity<>(presenter, HttpStatus.BAD_REQUEST);
		}

		final FacadeContext response = facade.calculate(logistic);

		return new ResponseEntity<>(response, response.isOk() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

}
