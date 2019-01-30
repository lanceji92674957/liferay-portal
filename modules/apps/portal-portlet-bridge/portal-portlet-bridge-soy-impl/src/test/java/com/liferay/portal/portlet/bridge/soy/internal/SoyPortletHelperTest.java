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

package com.liferay.portal.portlet.bridge.soy.internal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommandCache;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.util.HtmlImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

import org.osgi.framework.Bundle;

/**
 * @author Marcellus Tavares
 */
public class SoyPortletHelperTest {

	@Before
	public void setUp() {
		_setUpJSONFactoryUtil();
		_setUpHtmlUtil();
	}

	@Test(expected = Exception.class)
	public void testGetJavaScriptControllerModuleWithoutPackageJSON()
		throws Exception {

		SoyPortletHelper soyPortletHelper = new SoyPortletHelper(
			null, _mockEmptyMVCCommandCache(), null);

		soyPortletHelper.getJavaScriptLoaderModule("Path");
	}

	@Test
	public void testGetJavaScriptLoaderModule() throws Exception {
		SoyPortletHelper soyPortletHelper = new SoyPortletHelper(
			_mockBundleWithPackageFile(), _mockEmptyMVCCommandCache(), null);

		String javaScriptLoaderModule =
			soyPortletHelper.getJavaScriptLoaderModule("JavaScriptCommand");

		Assert.assertEquals(
			"package-with-version@1.0.0/JavaScriptCommand",
			javaScriptLoaderModule);
	}

	@Test
	public void testGetJavaScriptLoaderModuleForES6() throws Exception {
		SoyPortletHelper soyPortletHelper = new SoyPortletHelper(
			_mockBundleWithPackageFile(), _mockEmptyMVCCommandCache(), null);

		String javaScriptLoaderModule =
			soyPortletHelper.getJavaScriptLoaderModule("ES6Command");

		Assert.assertEquals(
			"package-with-version@1.0.0/ES6Command.es", javaScriptLoaderModule);
	}

	@Test
	public void testGetJavaScriptLoaderModuleForSoy() throws Exception {
		SoyPortletHelper soyPortletHelper = new SoyPortletHelper(
			_mockBundleWithPackageFile(), _mockEmptyMVCCommandCache(), null);

		String javaScriptLoaderModule =
			soyPortletHelper.getJavaScriptLoaderModule("SoyCommand");

		Assert.assertEquals(
			"package-with-version@1.0.0/SoyCommand.soy",
			javaScriptLoaderModule);
	}

	private Bundle _mockBundleWithPackageFile() {
		Bundle bundle = Mockito.mock(Bundle.class);

		Mockito.when(
			bundle.getEntry(Matchers.endsWith("ES6Command.es.js"))
		).thenReturn(
			SoyPortletHelperTest.class.getResource(
				"dependencies/ES6Command.es.js")
		);

		Mockito.when(
			bundle.getEntry(Matchers.endsWith("JavaScriptCommand.js"))
		).thenReturn(
			SoyPortletHelperTest.class.getResource(
				"dependencies/JavaScriptCommand.js")
		);

		Mockito.when(
			bundle.getEntry(Matchers.endsWith("package.json"))
		).thenReturn(
			SoyPortletHelperTest.class.getResource(
				"dependencies/package-with-version.json")
		);

		Mockito.when(
			bundle.getEntry(Matchers.endsWith("SoyCommand.soy"))
		).thenReturn(
			SoyPortletHelperTest.class.getResource(
				"dependencies/SoyCommand.soy")
		);

		return bundle;
	}

	private MVCCommandCache _mockEmptyMVCCommandCache() {
		return new MVCCommandCache<MVCRenderCommand>(
			MVCRenderCommand.EMPTY, StringPool.BLANK, StringPool.BLANK,
			MVCRenderCommand.class, StringPool.BLANK) {

			@Override
			public MVCRenderCommand getMVCCommand(String mvcCommandName) {
				return MVCRenderCommand.EMPTY;
			}

		};
	}

	private void _setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());
	}

	private void _setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

}