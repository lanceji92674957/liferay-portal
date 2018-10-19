/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.social.group.statistics.web.configuration.definition;

import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;
import com.liferay.social.group.statistics.web.configuration.SocialGroupStatisticsPortletInstanceConfiguration;

import org.osgi.service.component.annotations.Component;

/**
 * @author Iván Zaera
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,service = ConfigurationBeanDeclaration.class)
public class SocialGroupStatisticsPortletInstanceConfigurationBeanDeclaration
	implements ConfigurationBeanDeclaration {

	@Override
	public Class<?> getConfigurationBeanClass() {
		return SocialGroupStatisticsPortletInstanceConfiguration.class;
	}

}