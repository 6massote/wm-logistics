package br.com.walmart.freight.presenters;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.walmart.core.layers.LayerObject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FreightPresenter extends LayerObject {

}
