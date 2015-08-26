package br.com.walmart.freight.facades;

import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.freight.models.Logistic;

public interface FreightFacade {

	public abstract FacadeContext calculate(final Logistic logistic);
	
}
