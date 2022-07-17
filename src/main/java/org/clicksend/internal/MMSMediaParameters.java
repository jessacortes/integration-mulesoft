package org.clicksend.internal;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.display.PathModel.Type;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Path;

public class MMSMediaParameters {
	@Parameter
	@Path(type = Type.FILE)
	@Optional(defaultValue = "")
	@Expression(ExpressionSupport.SUPPORTED)
	String FilePath;
	
	@Parameter
	@Optional(defaultValue = "")
	@Expression(ExpressionSupport.SUPPORTED)
	String FileURL;
}
