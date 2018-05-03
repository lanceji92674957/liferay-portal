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

package com.liferay.captcha.taglib.util;

import com.liferay.captcha.util.CaptchaHelper;
import com.liferay.portal.kernel.captcha.Captcha;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lance Ji
 */
@Component(immediate = true)
public class CaptchaUtil {

	public static String getTaglibPath() {
		Captcha captcha = _captchaHelper.getCaptcha();

		return captcha.getTaglibPath();
	}

	public static boolean isEnabled(HttpServletRequest request) {
		Captcha captcha = _captchaHelper.getCaptcha();

		return captcha.isEnabled(request);
	}

	public static boolean isEnabled(PortletRequest portletRequest) {
		Captcha captcha = _captchaHelper.getCaptcha();

		return captcha.isEnabled(portletRequest);
	}

	@Reference
	private static CaptchaHelper _captchaHelper;

}