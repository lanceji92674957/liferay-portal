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

package com.liferay.portlet.display.template.exportimport.portlet.preferences.processor;

import com.liferay.exportimport.kernel.lar.PortletDataContext;

import javax.portlet.PortletPreferences;

/**
 * @author Lance Ji
 */
public interface CustomizedExportCapabilityRegister {

	public String getDisplayStyle(
		PortletDataContext portletDataContext, String portletId,
		PortletPreferences portletPreferences);

	public long getDisplayStyleGroupId(
		PortletDataContext portletDataContext, String portletId,
		PortletPreferences portletPreferences);

}