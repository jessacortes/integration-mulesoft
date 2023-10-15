package org.clicksend.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * This class (as it's name implies) provides connection instances and the
 * funcionality to disconnect and validate those connections.
 * <p>
 * All connection related parameters (values required in order to create a
 * connection) must be declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares
 * that connections resolved by this provider will be pooled and reused. There
 * are other implementations like {@link CachedConnectionProvider} which lazily
 * creates and caches connections or simply {@link ConnectionProvider} if you
 * want a new connection each time something requires one.
 */
public class ClickSendMule4sConnectionProvider implements PoolingConnectionProvider<ClickSendMule4sConnection> {

	private final Logger LOGGER = LoggerFactory.getLogger(ClickSendMule4sConnectionProvider.class);

	@Override
	public ClickSendMule4sConnection connect() throws ConnectionException {
		// TODO Auto-generated method stub
		return new ClickSendMule4sConnection();
	}

	@Override
	public void disconnect(ClickSendMule4sConnection connection) {
		try {
			connection.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while disconnecting: " + e.getMessage(), e);
		}
	}

	@Override
	public ConnectionValidationResult validate(ClickSendMule4sConnection connection) {
		return ConnectionValidationResult.success();
	}

}
