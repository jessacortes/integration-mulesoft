package org.clicksend.internal.config;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Sources;
import org.clicksend.internal.ClickSendMule4sConfiguration;
import org.mule.runtime.api.meta.Category;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "clicksend")
@Extension(name = "ClickSend", category = Category.CERTIFIED)
@Configurations(ClickSendMule4sConfiguration.class)
public class ClickSendMule4sExtensionConnector {

}
