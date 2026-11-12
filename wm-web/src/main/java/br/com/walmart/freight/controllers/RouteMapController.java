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
import br.com.walmart.freight.facades.RouteMapFacade;
import br.com.walmart.freight.models.RouteMap;
import br.com.walmart.freight.presenters.RouteMapPresenter;

@RestController
@RequestMapping("/maps")
public class RouteMapController {

	@Autowired
	RouteMapFacade facade;
	// Prototype scoped: a fresh presenter per request, otherwise errors would pile up across requests.
	@Autowired
	ObjectProvider<RouteMapPresenter> routeMapPresenters;

	@PostMapping
	public ResponseEntity<LayerObject> create(@Valid @RequestBody RouteMap maps, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final RouteMapPresenter presenter = routeMapPresenters.getObject();
			presenter.addErrors(BindingResultHumanErrorConverter.convert(bindingResult.getAllErrors()));
			return new ResponseEntity<>(presenter, HttpStatus.BAD_REQUEST);
		}

		final FacadeContext response = facade.create(maps);

		return new ResponseEntity<>(response, response.isOk() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

}
