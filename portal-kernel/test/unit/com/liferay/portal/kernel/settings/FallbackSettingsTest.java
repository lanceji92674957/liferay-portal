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
import com.liferay.portal.kernel.util.ObjectValuePair;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Iván Zaera
 */
public class FallbackSettingsTest {

	public FallbackSettingsTest() {
		_fallbackKeys = new FallbackKeys();

		_fallbackKeys.add("key1", "key2", "key3");
		_fallbackKeys.add("key2", "key7");
		_fallbackKeys.add("key3", "key5");
	}

	@Test
	public void testGetValuesWhenConfigured() {
		String[] defaultValues = {"default"};

		String[] mockValues = {"value"};

		Settings settings = ProxyTestUtil.getProxy(
			Settings.class,
			new ObjectValuePair<>(
				"getValues",
				args -> {
					if ("key2".equals(args[0]) && (null == args[1])) {
						return mockValues;
					}

					return null;
				}));

		FallbackSettings fallbackSettings = new FallbackSettings(
			settings, _fallbackKeys);

		String[] values = fallbackSettings.getValues("key1", defaultValues);

		Assert.assertArrayEquals(mockValues, values);

		verifyHasSettingValue(settings, "getValues", "key1", "key2");
	}

	@Test
	public void testGetValuesWhenUnconfigured() {
		Settings settings = ProxyTestUtil.getProxy(Settings.class);

		FallbackSettings fallbackSettings = new FallbackSettings(
			settings, _fallbackKeys);

		String[] defaultValues = {"default"};

		String[] values = fallbackSettings.getValues("key1", defaultValues);

		Assert.assertArrayEquals(defaultValues, values);

		verifyNoSettingValue(
			settings, "getValues", defaultValues, "key1", "key2", "key3");
	}

	@Test
	public void testGetValueWhenConfigured() {
		Settings settings = ProxyTestUtil.getProxy(
			Settings.class,
			new ObjectValuePair<>(
				"getValue",
				args -> {
					if ("key2".equals(args[0]) && (null == args[1])) {
						return "value";
					}

					return null;
				}));

		FallbackSettings fallbackSettings = new FallbackSettings(
			settings, _fallbackKeys);

		String value = fallbackSettings.getValue("key1", "default");

		Assert.assertEquals("value", value);

		verifyHasSettingValue(settings, "getValue", "key1", "key2");
	}

	@Test
	public void testGetValueWhenUnconfigured() {
		Settings settings = ProxyTestUtil.getProxy(Settings.class);

		FallbackSettings fallbackSettings = new FallbackSettings(
			settings, _fallbackKeys);

		String value = fallbackSettings.getValue("key1", "default");

		Assert.assertEquals("default", value);

		verifyNoSettingValue(
			settings, "getValue", "default", "key1", "key2", "key3");
	}

	protected void verifyHasSettingValue(
		Settings settings, String methodName, String... keys) {

		List<Object[]> argumentsList = ProxyTestUtil.getArgumentsList(
			settings, methodName);

		Assert.assertEquals(
			argumentsList.toString(), keys.length, argumentsList.size());

		for (int i = 0; i < keys.length; i++) {
			Assert.assertArrayEquals(
				new Object[] {keys[i], null}, argumentsList.get(i));
		}
	}

	protected void verifyNoSettingValue(
		Settings settings, String methodName, Object defaultValue,
		String... keys) {

		List<Object[]> argumentsList = ProxyTestUtil.getArgumentsList(
			settings, methodName);

		Assert.assertEquals(
			argumentsList.toString(), keys.length + 1, argumentsList.size());

		Assert.assertArrayEquals(
			new Object[] {keys[0], defaultValue}, argumentsList.get(0));

		for (int i = 1; i < keys.length; i++) {
			Assert.assertArrayEquals(
				new Object[] {keys[i], null}, argumentsList.get(i));
		}
	}

	private final FallbackKeys _fallbackKeys;

}