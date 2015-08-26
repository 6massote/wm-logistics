package br.com.walmart.freight.services;

import br.com.walmart.freight.models.Logistic;

public interface FindShortestPathService {

	public abstract Float baseOn(final Logistic logistic) throws Exception;
	
}
