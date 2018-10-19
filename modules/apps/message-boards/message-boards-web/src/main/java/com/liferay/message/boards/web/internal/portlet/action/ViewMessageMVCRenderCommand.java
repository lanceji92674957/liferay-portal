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

package com.liferay.message.boards.web.internal.portlet.action;

import com.liferay.message.boards.constants.MBPortletKeys;
import com.liferay.message.boards.exception.NoSuchMessageException;
import com.liferay.message.boards.model.MBMessageDisplay;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	property = {
		"javax.portlet.name=" + MBPortletKeys.MESSAGE_BOARDS,
		"javax.portlet.name=" + MBPortletKeys.MESSAGE_BOARDS_ADMIN,
		"mvc.command.name=/message_boards/view_message"
	},
	service = MVCRenderCommand.class
)
public class ViewMessageMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			MBMessageDisplay messageDisplay = ActionUtil.getMessageDisplay(
				renderRequest);

			renderRequest.setAttribute(
				WebKeys.MESSAGE_BOARDS_MESSAGE_DISPLAY, messageDisplay);

			return "/message_boards/view_message.jsp";
		}
		catch (NoSuchMessageException | PrincipalException e) {
			SessionErrors.add(renderRequest, e.getClass());

			return "/message_boards/error.jsp";
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

}