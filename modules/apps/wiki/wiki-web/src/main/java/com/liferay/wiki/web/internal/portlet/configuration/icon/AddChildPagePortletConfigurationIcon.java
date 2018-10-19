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

package com.liferay.wiki.web.internal.portlet.configuration.icon;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.wiki.constants.WikiPortletKeys;
import com.liferay.wiki.model.WikiNode;
import com.liferay.wiki.model.WikiPage;
import com.liferay.wiki.web.internal.portlet.action.ActionUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Roberto Díaz
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	immediate = true,
	property = {
		"javax.portlet.name=" + WikiPortletKeys.WIKI_ADMIN, "path=/wiki/view"
	},
	service = PortletConfigurationIcon.class
)
public class AddChildPagePortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getMessage(PortletRequest portletRequest) {
		return LanguageUtil.get(
			getResourceBundle(getLocale(portletRequest)), "add-child-page");
	}

	@Override
	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			WikiPage page = ActionUtil.getPage(portletRequest);

			PortletURL portletURL = _portal.getControlPanelPortletURL(
				portletRequest, WikiPortletKeys.WIKI_ADMIN,
				PortletRequest.RENDER_PHASE);

			portletURL.setParameter("mvcRenderCommandName", "/wiki/edit_page");
			portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
			portletURL.setParameter("nodeId", String.valueOf(page.getNodeId()));
			portletURL.setParameter("title", StringPool.BLANK);
			portletURL.setParameter("editTitle", "1");
			portletURL.setParameter("parentTitle", page.getTitle());

			return portletURL.toString();
		}
		catch (Exception e) {
		}

		return StringPool.BLANK;
	}

	@Override
	public double getWeight() {
		return 101;
	}

	@Override
	public boolean isShow(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			WikiPage page = ActionUtil.getPage(portletRequest);

			if (Validator.isNull(page.getContent())) {
				return false;
			}

			return _wikiNodeModelResourcePermission.contains(
				themeDisplay.getPermissionChecker(), page.getNodeId(),
				ActionKeys.ADD_PAGE);
		}
		catch (Exception e) {
		}

		return false;
	}

	@Reference
	private Portal _portal;

	@Reference(target = "(model.class.name=com.liferay.wiki.model.WikiNode)")
	private ModelResourcePermission<WikiNode> _wikiNodeModelResourcePermission;

}