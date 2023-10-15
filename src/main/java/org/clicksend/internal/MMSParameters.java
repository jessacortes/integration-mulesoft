package org.clicksend.internal;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.display.PathModel.Type;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Path;

public class MMSParameters {
	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	String To;
	
	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	String Subject;
	
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