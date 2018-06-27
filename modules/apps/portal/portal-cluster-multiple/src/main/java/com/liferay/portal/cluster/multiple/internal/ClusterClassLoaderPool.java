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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Lance Ji
 */
public class ClusterClassLoaderPool {

	public static ClassLoader getClassLoader(String contextName) {
		ClassLoader classLoader = null;

		if ((contextName != null) && !contextName.equals("null")) {
			classLoader = _classLoaders.get(contextName);

			if (classLoader == null) {
				List<VersionedClassLoader> classLoadersInOrder =
					_fallbackClassLoaders.get(_getSymbolicName(contextName));

				if (classLoadersInOrder != null) {
					VersionedClassLoader latestVersionClassLoader =
						classLoadersInOrder.get(0);

					classLoader = latestVersionClassLoader.getClassLoader();
				}
			}
		}

		if (classLoader == null) {
			Thread currentThread = Thread.currentThread();

			classLoader = currentThread.getContextClassLoader();
		}

		return classLoader;
	}

	public static String getContextName(ClassLoader classLoader) {
		if (classLoader == null) {
			return "null";
		}

		String contextName = _contextNames.get(classLoader);

		if (contextName == null) {
			contextName = "null";
		}

		return contextName;
	}

	public static void register(String[] bundleInfo, ClassLoader classLoader) {
		String contextName = _toContextName(bundleInfo);

		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);

		_registerFallback(bundleInfo, classLoader);
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);
		}
	}

	private static String _getSymbolicName(String contextName) {
		int pos = contextName.indexOf("_");

		if (pos < 0) {
			return contextName;
		}

		return contextName.substring(0, pos);
	}

	private static void _registerFallback(
		String[] bundleInfo, ClassLoader classLoader) {

		if (bundleInfo[1] == null) {
			return;
		}

		List<VersionedClassLoader> versionedClassLoaders =
			_fallbackClassLoaders.get(bundleInfo[0]);

		if (versionedClassLoaders == null) {
			versionedClassLoaders = new CopyOnWriteArrayList<>();
		}

		versionedClassLoaders.add(
			new VersionedClassLoader(classLoader, bundleInfo[1]));

		if (versionedClassLoaders.size() > 1) {
			Collections.sort(versionedClassLoaders);
		}

		_fallbackClassLoaders.put(bundleInfo[0], versionedClassLoaders);
	}

	private static String _toContextName(String[] bundleInfo) {
		return bundleInfo[0].concat(StringPool.UNDERLINE).concat(bundleInfo[1]);
	}

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();
	private static final Map<String, List<VersionedClassLoader>>
		_fallbackClassLoaders = new ConcurrentHashMap<>();

	static {
		register(
			new String[] {"GlobalClassLoader", ""},
			ClassLoaderPool.class.getClassLoader());
		register(
			new String[] {StringPool.BLANK, ""},
			ClassLoaderPool.class.getClassLoader());
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
			String[] array = s.split("\\.");

			int[] newArray = new int[array.length];

			for (int i = 0; i < array.length; i++) {
				int value = 0;

				try {
					value = Integer.parseInt(array[i]);
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