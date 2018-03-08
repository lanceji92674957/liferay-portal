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

package com.liferay.blogs.web.internal.exportimport.portlet.preferences.processor;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portlet.display.template.PortletDisplayTemplateConstants;
import com.liferay.portlet.display.template.exportimport.portlet.preferences.processor.PortletDisplayTemplateCapabilityRegister;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;

/**
 * @author Mate Thurzo
 */
@Component(
	immediate = true,
	property = {
		"name=BlogsExportCapabilityRegister",
		"type=" + PortletDisplayTemplateConstants.DATA_EXPORT_HANDLE
	},
	service = BlogsPortletDisplayTemplateExportCapabilityRegister.class
)
public class BlogsPortletDisplayTemplateExportCapabilityRegister
	implements PortletDisplayTemplateCapabilityRegister {

	@Override
	public String getDisplayStyle(
		PortletDataContext portletDataContext, String portletId,
		PortletPreferences portletPreferences) {

		return BlogsExportImportPortletPreferencesProcessorUtil.getDisplayStyle(
			portletPreferences);
	}

	@Override
	public long getDisplayStyleGroupId(
		PortletDataContext portletDataContext, String portletId,
		PortletPreferences portletPreferences) {

		return BlogsExportImportPortletPreferencesProcessorUtil.
			getDisplayStyleGroupId(portletPreferences);
	}

}