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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lance Ji
 */
public class ClusterClassLoaderPool {

	public static void register(String[] bundleInfo, ClassLoader classLoader) {
		String contextName = bundleInfo[0];

		if (bundleInfo[1] != null) {
			contextName = StringBundler.concat(
				contextName, StringPool.UNDERLINE, bundleInfo[1]);
		}

		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);
		}
	}

	private static final String _PORTAL_SERVLETCONTEXTNAME = StringPool.BLANK;

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();

	static {
		register(
			new String[] {"GlobalClassLoader", null},
			ClusterClassLoaderPool.class.getClassLoader());
		register(
			new String[] {_PORTAL_SERVLETCONTEXTNAME, null},
			ClusterClassLoaderPool.class.getClassLoader());
	}

	private static class VersionedClassLoader
		implements Comparable<VersionedClassLoader> {

		@Override
		public int compareTo(VersionedClassLoader versionedClassLoader) {
			int[] comparedVersion = versionedClassLoader.getSplitVersion();
			int i = 0;

			while ((i < _splitVersion.length) && (i < comparedVersion.length) &&
				   (_splitVersion[i] == comparedVersion[i])) {

				i++;
			}

			if ((i < _splitVersion.length) && (i < comparedVersion.length)) {
				int diff = comparedVersion[i] - _splitVersion[i];

				return Integer.signum(diff);
			}

			return Integer.signum(
				comparedVersion.length - _splitVersion.length);
		}

		public ClassLoader getClassLoader() {
			return _classLoader;
		}

		public int[] getSplitVersion() {
			return _splitVersion;
		}

		public String getVersion() {
			return _version;
		}

		private VersionedClassLoader(ClassLoader classLoader, String version) {
			_classLoader = classLoader;
			_version = version;

			_splitVersion = _split(version);
		}

		private int[] _split(String s) {
			List<String> array = StringUtil.split(s, CharPool.PERIOD);

			int[] newArray = new int[array.size()];

			for (int i = 0; i < array.size(); i++) {
				int value = 0;

				try {
					value = Integer.parseInt(array.get(i));
				}
				catch (Exception e) {
				}

				newArray[i] = value;
			}

			return newArray;
		}

		private final ClassLoader _classLoader;
		private final int[] _splitVersion;
		private final String _version;

	}

}