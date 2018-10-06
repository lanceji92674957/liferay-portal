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

import com.liferay.portal.kernel.util.ProxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Lance Ji
 */
public class ProxyTestUtil {

	public static boolean assertProxyBehavior() {

	}

	public static <T> T getDummyProxy(Class<T> clazz) {
		return (T)ProxyUtil.newProxyInstance(
			clazz.getClassLoader(), new Class<?>[] {clazz},
			(proxy, method, args) -> method.getDefaultValue());
	}

	public static <T> T getProxy(
		Class<T> clazz, String methodName,
		Function<Object[], Object> function) {

		ProxyTestInvocationHandler proxyTestInvocationHandler =
			new ProxyTestInvocationHandler();

		proxyTestInvocationHandler.registerMethod(methodName, function);

		return (T)ProxyUtil.newProxyInstance(
			clazz.getClassLoader(), new Class<?>[] {clazz},
			proxyTestInvocationHandler);
	}

	public static <T> T getProxy(
		Class<T> clazz, String methodName, Object expectedResult) {

		ProxyTestInvocationHandler proxyTestInvocationHandler =
			new ProxyTestInvocationHandler();

		proxyTestInvocationHandler.registerMethod(methodName, expectedResult);

		return (T)ProxyUtil.newProxyInstance(
			clazz.getClassLoader(), new Class<?>[] {clazz},
			proxyTestInvocationHandler);
	}

	public static ProxyBehavior getProxyBehaviorInstance(
		Proxy proxy, String methodName, Object[] args) {

		return new ProxyBehavior(proxy, methodName, args);
	}

	private static class ProxyTestInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			Object result = _results.get(method.getName());

			if (result != null) {
				if (result instanceof Exception) {
					throw (Exception)result;
				}
				else if (result instanceof Function) {
					return ((Function)result).apply(args);
				}

				return result;
			}

			return method.getDefaultValue();
		}

		public void registerMethod(String methodName, Object expectedResult) {
			_results.put(methodName, expectedResult);
		}

		private ProxyTestInvocationHandler() {
			_results = new HashMap<>();
		}

		private final Map<String, Object> _results;

	}

	private static class ProxyBehavior {
		public ProxyBehavior(
			Proxy proxy, String methodName, Object[] arguments) {
			_proxy = proxy;
			_methodName = methodName;
			_arguments = arguments;
		}

		private final Proxy _proxy;
		private final String _methodName;
		private final Object[] _arguments;

	}

}