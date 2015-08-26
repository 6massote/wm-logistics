package br.com.walmart.freight.services;

import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;

public interface CalculateFreightService {

	public abstract Freight baseOn(final Logistic logistic, final Float shortestPathWeight) throws Exception;
	
}
