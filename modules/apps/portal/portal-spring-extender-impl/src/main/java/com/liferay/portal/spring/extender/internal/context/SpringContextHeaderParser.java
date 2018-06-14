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

package com.liferay.portal.spring.extender.internal.context;

import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 */
public class SpringContextHeaderParser {

	public SpringContextHeaderParser(Bundle bundle) {
		_bundle = bundle;
	}

	public String[] getBeanDefinitionFileNames() {
		List<String> beanDefinitionFileNames = new ArrayList<>();

		Dictionary<String, String> headers = _bundle.getHeaders(
			StringPool.BLANK);

		String liferayService = headers.get("Liferay-Service");

		if (liferayService != null) {
			beanDefinitionFileNames.add("META-INF/spring/parent");
		}

		String springContext = headers.get("Liferay-Spring-Context");

		if (springContext != null) {
			Collections.addAll(
				beanDefinitionFileNames, springContext.split(","));
		}

		return beanDefinitionFileNames.toArray(
			new String[beanDefinitionFileNames.size()]);
	}

	private final Bundle _bundle;

}