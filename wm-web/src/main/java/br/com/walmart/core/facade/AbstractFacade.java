package br.com.walmart.core.facade;

import br.com.walmart.core.layers.FacadeContext;

public class AbstractFacade {
	
	public FacadeContext getFacadeContext() {
		return new FacadeContext();
	}
	
}
