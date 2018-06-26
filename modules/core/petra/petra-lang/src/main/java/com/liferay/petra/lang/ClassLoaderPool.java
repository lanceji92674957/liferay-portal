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

package com.liferay.petra.lang;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Maps servlet context names to/from the servlet context's class loader.
 *
 * @author Shuyang Zhou
 */
public class ClassLoaderPool {

	/**
	 * Returns the class loader associated with the context name.
	 *
	 * <p>
	 * If no class loader is found for the context name, the thread's context
	 * class loader is returned as a fallback.
	 * </p>
	 *
	 * @param  contextName the servlet context's name
	 * @return the class loader associated with the context name
	 */
	public static ClassLoader getClassLoader(String contextName) {
		ClassLoader classLoader = null;

		if ((contextName != null) && !contextName.equals("null")) {
			classLoader = _classLoaders.get(contextName);
		}

		if (classLoader == null) {
			Thread currentThread = Thread.currentThread();

			classLoader = currentThread.getContextClassLoader();
		}

		return classLoader;
	}

	/**
	 * Returns the context name associated with the class loader.
	 *
	 * <p>
	 * If the class loader is <code>null</code> or if no context name is
	 * associated with the class loader, {@link
	 * <code>"<code>null</code>"</code>} is returned.
	 * </p>
	 *
	 * @param  classLoader the class loader
	 * @return the context name associated with the class loader
	 */
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

	public static void register(String contextName, ClassLoader classLoader) {
		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);

		_registerFallback(contextName, classLoader);
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);
		}
	}

	public static void unregister(String contextName) {
		ClassLoader classLoader = _classLoaders.remove(contextName);

		if (classLoader != null) {
			_contextNames.remove(classLoader);
		}
	}

	private static int _compareVersions(String version1, String version2) {
		int[] splitVersion1 = _split(version1);
		int[] splitVersion2 = _split(version2);

		int i = 0;

		while ((i < splitVersion1.length) && (i < splitVersion2.length) &&
			   (splitVersion1[i] == splitVersion2[i])) {

			i++;
		}

		if ((i < splitVersion1.length) && (i < splitVersion2.length)) {
			int diff = splitVersion2[i] - splitVersion1[i];

			return Integer.signum(diff);
		}

		return Integer.signum(splitVersion2.length - splitVersion1.length);
	}

	private static String _getSymbolicName(String contextName) {
		int pos = contextName.indexOf("_");

		if (pos < 0) {
			return contextName;
		}

		return contextName.substring(0, pos);
	}

	private static String _getVersion(String contextName) {
		int pos = contextName.indexOf("_");

		if (pos < 0) {
			return null;
		}

		return contextName.substring(pos + 1);
	}

	private static void _registerFallback(
		String contextName, ClassLoader classLoader) {

		String version = _getVersion(contextName);

		if (version == null) {
			return;
		}

		String symbolicName = _getSymbolicName(contextName);

		List<VersionedClassLoader> versionedClassLoaders =
			_fallbackClassLoaders.get(symbolicName);

		if (versionedClassLoaders == null) {
			versionedClassLoaders = new CopyOnWriteArrayList<>();
		}

		versionedClassLoaders.add(
			new VersionedClassLoader(version, classLoader));

		if (versionedClassLoaders.size() > 1) {
			Collections.sort(versionedClassLoaders);
		}

		_fallbackClassLoaders.put(symbolicName, versionedClassLoaders);
	}

	private static int[] _split(String s) {
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

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();
	private static final Map<String, List<VersionedClassLoader>>
		_fallbackClassLoaders = new ConcurrentHashMap<>();

	static {
		register("GlobalClassLoader", ClassLoaderPool.class.getClassLoader());
	}

	private static class VersionedClassLoader
		implements Comparable<VersionedClassLoader> {

		@Override
		public int compareTo(VersionedClassLoader versionedClassLoader) {
			return _compareVersions(
				getVersion(), versionedClassLoader.getVersion());
		}

		public ClassLoader getClassLoader() {
			return _classLoader;
		}

		public String getVersion() {
			return _version;
		}

		private VersionedClassLoader(String version, ClassLoader classLoader) {
			_version = version;
			_classLoader = classLoader;
		}

		private final ClassLoader _classLoader;
		private final String _version;

	}

}