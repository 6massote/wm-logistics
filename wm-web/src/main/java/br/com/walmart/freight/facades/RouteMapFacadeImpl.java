package br.com.walmart.freight.facades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.core.facade.AbstractFacade;
import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.freight.models.RouteMap;
import br.com.walmart.freight.services.PopulateRoutesService;

@Service("routeMapFacade")
public class RouteMapFacadeImpl extends AbstractFacade implements RouteMapFacade {

	@Autowired
	private PopulateRoutesService populate;
	
	public FacadeContext create(final RouteMap routeMap) {
		final FacadeContext facadeContext = getFacadeContext();
		
		try {
			populate.inGraph(routeMap.getName(), routeMap.getRoutes());
			
			facadeContext.addMessage("Maps populated in Graph...");
		} catch (Exception e) {
			facadeContext.addError(e.getMessage());
			e.printStackTrace();
		}
		
		return facadeContext;
	}
	
}
