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

package com.liferay.server.admin.web.internal.portlet;

import com.liferay.portal.kernel.portlet.ControlPanelEntry;
import com.liferay.portal.kernel.portlet.OmniadminControlPanelEntry;
import com.liferay.portal.kernel.util.PortletKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alan Huang
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	immediate = true,
	property = "javax.portlet.name=" + PortletKeys.SERVER_ADMIN,
	service = ControlPanelEntry.class
)
public class ServerAdminControlPanelEntry extends OmniadminControlPanelEntry {
}