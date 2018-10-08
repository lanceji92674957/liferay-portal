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

package com.liferay.portal.kernel.test;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Lance Ji
 */
public class ProxyTestUtil {

	public static List<Object[]> getArgumentsList(
		Object proxy, String methodName) {

		Map<String, List<Object[]>> proxyActions = getProxyActions(proxy);

		return proxyActions.get(methodName);
	}

	public static <T> T getDummyProxy(Class<T> interfaceClass) {
		return (T)ProxyUtil.newProxyInstance(
			_getClassLoader(interfaceClass), new Class<?>[] {interfaceClass},
			new DummyInvocationHandler());
	}

	public static <T> T getProxy(
		Class<T> interfaceClass,
		ObjectValuePair
			<String, Function<Object[], Object>>... objectValuePairs) {

		HashMap<String, Function<Object[], Object>> functions = new HashMap<>();

		for (ObjectValuePair<String, Function<Object[], Object>>
				objectValuePair : objectValuePairs) {

			functions.put(objectValuePair.getKey(), objectValuePair.getValue());
		}

		ProxyTestInvocationHandler proxyTestInvocationHandler =
			new ProxyTestInvocationHandler(functions);

		return (T)ProxyUtil.newProxyInstance(
			_getClassLoader(interfaceClass), new Class<?>[] {interfaceClass},
			proxyTestInvocationHandler);
	}

	public static Map<String, List<Object[]>> getProxyActions(Object proxy) {
		InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(
			proxy);

		if (invocationHandler instanceof ProxyTestInvocationHandler) {
			ProxyTestInvocationHandler proxyTestInvocationHandler =
				(ProxyTestInvocationHandler)invocationHandler;

			return proxyTestInvocationHandler.getProxyActions();
		}

		throw new IllegalArgumentException("Not a proxy test instance");
	}

	private static ClassLoader _getClassLoader(Class<?> clazz) {
		ClassLoader classLoader = clazz.getClassLoader();

		if (classLoader == null) {
			classLoader = ProxyTestUtil.class.getClassLoader();
		}

		return classLoader;
	}

	private static class DummyInvocationHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {

			Class<?> returnType = method.getReturnType();

			if (returnType.equals(boolean.class)) {
				return GetterUtil.DEFAULT_BOOLEAN;
			}
			else if (returnType.equals(byte.class)) {
				return GetterUtil.DEFAULT_BYTE;
			}
			else if (returnType.equals(double.class)) {
				return GetterUtil.DEFAULT_DOUBLE;
			}
			else if (returnType.equals(float.class)) {
				return GetterUtil.DEFAULT_FLOAT;
			}
			else if (returnType.equals(int.class)) {
				return GetterUtil.DEFAULT_INTEGER;
			}
			else if (returnType.equals(long.class)) {
				return GetterUtil.DEFAULT_LONG;
			}
			else if (returnType.equals(short.class)) {
				return GetterUtil.DEFAULT_SHORT;
			}

			return method.getDefaultValue();
		}

	}

	private static class ProxyTestInvocationHandler
		extends DummyInvocationHandler {

		public Map<String, List<Object[]>> getProxyActions() {
			return _proxyActions;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			String methodName = method.getName();

			List<Object[]> argumentsList = _proxyActions.get(methodName);

			if (argumentsList == null) {
				argumentsList = new ArrayList<>();

				_proxyActions.put(methodName, argumentsList);
			}

			argumentsList.add(args);

			Function<Object[], Object> expectedFunction = _functions.get(
				methodName);

			if (expectedFunction != null) {
				return expectedFunction.apply(args);
			}

			return super.invoke(proxy, method, args);
		}

		private ProxyTestInvocationHandler(
			Map<String, Function<Object[], Object>> functions) {

			_functions = functions;
		}

		private final Map<String, Function<Object[], Object>> _functions;
		private final Map<String, List<Object[]>> _proxyActions =
			new HashMap<>();

	}

}