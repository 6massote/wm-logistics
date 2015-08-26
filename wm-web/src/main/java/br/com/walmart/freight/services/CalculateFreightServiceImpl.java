package br.com.walmart.freight.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;

@Service("calculateFreightService")
public class CalculateFreightServiceImpl implements CalculateFreightService {

	public Freight baseOn(final Logistic logistic, Float shortestPathWeight) throws Exception {
		System.out.println(">>>> Calculate Freight base on Logistics");
		
		if (logistic == null || shortestPathWeight == null) {
			return new Freight(-1f);
		}
		
		final BigDecimal autonomy = new BigDecimal(logistic.getAutonomy());
		final BigDecimal price = new BigDecimal(logistic.getPrice());
		final BigDecimal weight = new BigDecimal(shortestPathWeight);
		
		final BigDecimal kml = weight.divide(autonomy);
		final BigDecimal consumption = kml.multiply(price);
		
		System.out.println(">>>> Final consumption: " + consumption.floatValue());
		
		return new Freight(consumption.floatValue());	
	}
}
