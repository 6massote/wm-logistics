package br.com.walmart.freight.facades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.core.facade.AbstractFacade;
import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.services.CalculateFreightService;
import br.com.walmart.freight.services.FindShortestPathService;


@Service("freightFacade")
public class FreightFacadeImpl extends AbstractFacade implements FreightFacade {

	@Autowired
	private FindShortestPathService findShortestPath;
	@Autowired
	private CalculateFreightService calculate;
	
	public FacadeContext calculate(final Logistic logistic) {
		final FacadeContext facadeContext = getFacadeContext();
		
		try {
			final Float shortestPathWeight = findShortestPath.baseOn(logistic);
			final Freight freight = calculate.baseOn(logistic, shortestPathWeight);
			
			facadeContext
				.addModel("freight", freight)
				.addMessage("Freight calc success...");
		} catch (Exception e) {
			facadeContext.addError(e.getMessage());
		}
		
		return facadeContext;
	}
	
}
