package br.com.walmart.freight.services;

import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;

public interface CalculateFreightService {

	Freight baseOn(Logistic logistic, Float shortestPathWeight);

}
