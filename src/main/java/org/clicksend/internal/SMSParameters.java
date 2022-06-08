package org.clicksend.internal;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class SMSParameters {
	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	String To;
	
	@Parameter
	@Optional
	@Expression(ExpressionSupport.SUPPORTED)
	String From;
	
	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	String Message;
	
	@Parameter
	@Optional
	@Expression(ExpressionSupport.SUPPORTED)
	String CustomString;
}
