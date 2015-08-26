package br.com.walmart.core.layers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public abstract class LayerObject {

	private final Map<String, Object> models 	= new HashMap<String, Object>();
	private final Set<String> errors 			= new HashSet<String>();
	private final Set<String> messages 			= new HashSet<String>();
	
	public LayerObject addModel(String key, Object model) {
		this.models.put(key, model);
		
		return ((LayerObject) this);
	}

	public <T> T getModel(String key, Class<T> klass) {
		return (T) this.models.get(key);
	}
	
	public Collection<Object> getModels() {
		return this.models.isEmpty() ? null : this.models.values();
	}

	public LayerObject addError(String message) {
		this.errors.add(message);	
		
		return ((LayerObject) this);
	}
	
	public LayerObject addErrors(Collection<String> errors) {
		this.errors.addAll(errors);	
		
		return ((LayerObject) this);
	}

	public Set<String> getErrors() {
		return this.errors.isEmpty() ? null : this.errors;
	}

	public LayerObject addMessage(String message) {
		this.messages.add(message);	
		
		return ((LayerObject) this);
	}
	
	public Set<String> getMessages() {
		return this.messages.isEmpty() ? null : this.messages;
	}

	public boolean isOk() {
		return this.errors != null && this.errors.isEmpty();
	}
}
