package br.com.walmart.freight.facades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.core.facade.AbstractFacade;
import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.freight.models.RouteMap;
import br.com.walmart.freight.services.PopulateRoutesService;

@Service("routeMapFacade")
public class RouteMapFacadeImpl extends AbstractFacade implements RouteMapFacade {

	private static final Logger logger = LoggerFactory.getLogger(RouteMapFacadeImpl.class);

	@Autowired
	private PopulateRoutesService populate;

	@Override
	public FacadeContext create(final RouteMap routeMap) {
		final FacadeContext facadeContext = getFacadeContext();

		try {
			populate.inGraph(routeMap.getName(), routeMap.getRoutes());

			facadeContext.addMessage("Maps populated in Graph...");
		} catch (RuntimeException e) {
			logger.error("Error populating map [{}]", routeMap.getName(), e);
			facadeContext.addError("error.system.route_map.populate_failed");
		}

		return facadeContext;
	}

}
