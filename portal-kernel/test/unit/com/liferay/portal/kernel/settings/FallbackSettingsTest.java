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

package com.liferay.portal.kernel.settings;

import com.liferay.portal.kernel.test.ProxyTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Iván Zaera
 */
public class FallbackSettingsTest {

	public FallbackSettingsTest() {
		_settings = ProxyTestUtil.getProxy(Settings.class);

		_fallbackKeys = new FallbackKeys();

		_fallbackKeys.add("key1", "key2", "key3");
		_fallbackKeys.add("key2", "key7");
		_fallbackKeys.add("key3", "key5");

		_fallbackSettings = new FallbackSettings(_settings, _fallbackKeys);
	}

	@Test
	public void testGetValuesWhenConfigured() {
		String[] defaultValues = {"default"};

		String[] mockValues = {"value"};

		ProxyTestUtil.updateProxy(
			_settings,
			ProxyTestUtil.getProxyMethod(
				"getValues",
				(Object[] args) -> {
					if ("key2".equals(args[0]) && (null == args[1])) {
						return mockValues;
					}

					return null;
				}));

		String[] values = _fallbackSettings.getValues("key1", defaultValues);

		Assert.assertArrayEquals(mockValues, values);

		verifyHasSettingValue("getValues", "key1", "key2");
	}

	@Test
	public void testGetValuesWhenUnconfigured() {
		String[] defaultValues = {"default"};

		String[] values = _fallbackSettings.getValues("key1", defaultValues);

		Assert.assertArrayEquals(defaultValues, values);

		verifyNoSettingValue(
			"getValues", defaultValues, "key1", "key2", "key3");
	}

	@Test
	public void testGetValueWhenConfigured() {
		ProxyTestUtil.updateProxy(
			_settings,
			ProxyTestUtil.getProxyMethod(
				"getValue",
				(Object[] args) -> {
					if ("key2".equals(args[0]) && (null == args[1])) {
						return "value";
					}

					return null;
				}));

		String value = _fallbackSettings.getValue("key1", "default");

		Assert.assertEquals("value", value);

		verifyHasSettingValue("getValue", "key1", "key2");
	}

	@Test
	public void testGetValueWhenUnconfigured() {
		String value = _fallbackSettings.getValue("key1", "default");

		Assert.assertEquals("default", value);

		verifyNoSettingValue("getValue", "default", "key1", "key2", "key3");
	}

	protected void verifyHasSettingValue(String methodName, String... keys) {
		List<ProxyTestUtil.ProxyAction> proxyActions =
			_getNoDefaultValueActions(methodName, keys);

		ProxyTestUtil.assertAction(_settings, proxyActions);
	}

	protected void verifyNoSettingValue(
		String methodName, Object defaultValue, String... keys) {

		List<ProxyTestUtil.ProxyAction> proxyActions =
			_getNoDefaultValueActions(methodName, keys);

		proxyActions.add(
			ProxyTestUtil.getProxyAction(
				methodName, new Object[] {keys[0], defaultValue}));

		ProxyTestUtil.assertAction(_settings, proxyActions);
	}

	private List<ProxyTestUtil.ProxyAction> _getNoDefaultValueActions(
		String methodName, String... keys) {

		List<ProxyTestUtil.ProxyAction> proxyActions = new ArrayList<>();

		for (String key : keys) {
			proxyActions.add(
				ProxyTestUtil.getProxyAction(
					methodName, new Object[] {key, null}));
		}

		return proxyActions;
	}

	private final FallbackKeys _fallbackKeys;
	private final FallbackSettings _fallbackSettings;
	private final Settings _settings;

}