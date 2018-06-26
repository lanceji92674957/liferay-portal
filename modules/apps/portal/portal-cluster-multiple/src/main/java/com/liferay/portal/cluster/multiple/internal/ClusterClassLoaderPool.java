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

package com.liferay.portal.cluster.multiple.internal;

import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.petra.string.StringPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lance Ji
 */
public class ClusterClassLoaderPool {

	public static void register(String[] bundleInfo, ClassLoader classLoader) {
		String contextName = _toContextName(bundleInfo);

		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);
		}
	}

	private static String _toContextName(String[] bundleInfo) {
		return bundleInfo[0].concat(StringPool.UNDERLINE).concat(bundleInfo[1]);
	}

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();

	static {
		register(
			new String[] {"GlobalClassLoader", ""},
			ClassLoaderPool.class.getClassLoader());
		register(
			new String[] {StringPool.BLANK, ""},
			ClassLoaderPool.class.getClassLoader());
	}

}