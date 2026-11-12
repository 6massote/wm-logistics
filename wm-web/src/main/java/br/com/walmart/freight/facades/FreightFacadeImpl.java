package br.com.walmart.freight.facades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(FreightFacadeImpl.class);

	@Autowired
	private FindShortestPathService findShortestPath;
	@Autowired
	private CalculateFreightService calculate;

	@Override
	public FacadeContext calculate(final Logistic logistic) {
		final FacadeContext facadeContext = getFacadeContext();

		try {
			final Float shortestPathWeight = findShortestPath.baseOn(logistic);

			if (shortestPathWeight == null || shortestPathWeight < 0) {
				facadeContext.addError("error.freight.calculate.no_route_found");
				return facadeContext;
			}

			final Freight freight = calculate.baseOn(logistic, shortestPathWeight);

			facadeContext
				.addModel("freight", freight)
				.addMessage("Freight calc success...");
		} catch (RuntimeException e) {
			logger.error("Error calculating freight", e);
			facadeContext.addError("error.system.freight.calculate_failed");
		}

		return facadeContext;
	}

}
