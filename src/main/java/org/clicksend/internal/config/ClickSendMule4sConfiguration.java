package org.clicksend.internal.config;

import org.clicksend.internal.ClickSendMule4sOperations;
import org.clicksend.internal.connection.provider.ClickSendMule4sConnectionProvider;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.springframework.core.annotation.Order;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(ClickSendMule4sOperations.class)
@ConnectionProviders(ClickSendMule4sConnectionProvider.class)
public class ClickSendMule4sConfiguration {

  @Parameter
  @Placement(tab = "DEFAULT_TAB")
  @DisplayName(value = "Username/Email")
  @Order(value = 1)
  @Example(value = "abc@xyz.com")
  private String userId;
  
  @Parameter
  @Placement(tab = "DEFAULT_TAB")
  @DisplayName(value = "Password")
  @Order(value = 2)
  @Password
  private String password;

  public String getUserId(){
    return userId;
  }
  
  public String getPassword(){
	    return password;
	  }

public void setUserId(String userId) {
	this.userId = userId;
}

public void setPassword(String password) {
	this.password = password;
}
}
