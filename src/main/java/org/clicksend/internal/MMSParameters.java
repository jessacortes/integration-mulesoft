package org.clicksend.internal;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Placement;

public class MMSParameters {
	@Parameter
	@Placement
	@Expression(ExpressionSupport.SUPPORTED)
	String To;
	
	@Parameter
	@Placement
	@Expression(ExpressionSupport.SUPPORTED)
	String Subject;
	
	@Parameter
	@Optional
	@Expression(ExpressionSupport.SUPPORTED)
	String From;
	
	@Parameter
	@Placement
	@Expression(ExpressionSupport.SUPPORTED)
	String Message;
	
	@Parameter
	@Optional
	@Expression(ExpressionSupport.SUPPORTED)
	String CustomString;

	public void setTo(String to) {
		To = to;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public void setFrom(String from) {
		From = from;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public void setCustomString(String customString) {
		CustomString = customString;
	}
}