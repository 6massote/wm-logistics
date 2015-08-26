package br.com.walmart.core.converters;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.validation.ObjectError;

public class BindingResultHumanErrorConverter {

	public static Set<String> convert(final Collection<ObjectError> validationErrors) {
		
		if (validationErrors == null || validationErrors.isEmpty())
			return Collections.emptySet();
		
		final Set<String> humanErrors = new HashSet<String>();
		
		for (Iterator<ObjectError> errorsIterator = validationErrors.iterator(); errorsIterator.hasNext();){
			final ObjectError error = errorsIterator.next();
			
			if (error != null && error.getDefaultMessage() != null)
				humanErrors.add(error.getDefaultMessage());
	    }
		
		return humanErrors;
	}
	
}
