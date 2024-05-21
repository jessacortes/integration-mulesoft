package org.clicksend.internal;

import org.clicksend.internal.connection.provider.ClickSendMule4sConnectionProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mule.runtime.api.connection.ConnectionException;

public class ConnectionProviderTest {

	@Test
    public void connectionTest() throws ConnectionException
    {
		ClickSendMule4sConnectionProvider c = new ClickSendMule4sConnectionProvider();
		Assert.assertNotNull(c.connect());
		Assert.assertNotNull(c.validate(c.connect()));
		c.disconnect(new ClickSendMule4sConnection());
		
    }
}
