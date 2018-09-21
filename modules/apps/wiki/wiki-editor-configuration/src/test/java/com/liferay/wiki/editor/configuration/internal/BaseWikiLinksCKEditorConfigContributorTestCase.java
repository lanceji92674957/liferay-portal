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

package com.liferay.wiki.editor.configuration.internal;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletURL;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Lance Ji
 */
public abstract class BaseWikiLinksCKEditorConfigContributorTestCase {

	@Before
	public void setUp() {
		_itemSelector = Mockito.mock(ItemSelector.class);

		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());

		_requestBackedPortletURLFactory = ProxyFactory.newDummyInstance(
			RequestBackedPortletURLFactory.class);

		_inputEditorTaglibAttributes.put(
			"liferay-ui:input-editor:name", "testEditor");

		baseWikiLinksCKEditorConfigContributor =
			getWikiLinksCKEditorConfigContributor();

		ReflectionTestUtil.setFieldValue(
			baseWikiLinksCKEditorConfigContributor, "itemSelector",
			_itemSelector);

		_themeDisplay = new ThemeDisplay();

		ReflectionTestUtil.setFieldValue(_themeDisplay, "_locale", null);
	}

	@Test
	public void testItemSelectorURLWhenNullWikiPageAndValidNode()
		throws Exception {

		populateInputEditorWikiPageAttributes(0, 1);

		JSONObject originalJSONObject =
			getJSONObjectWithDefaultItemSelectorURL();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		PortletURL itemSelectorPortletURL = Mockito.mock(PortletURL.class);

		Mockito.when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"oneTabItemSelectorPortletURL"
		);

		Mockito.when(
			_itemSelector.getItemSelectorURL(
				Matchers.any(RequestBackedPortletURLFactory.class),
				Matchers.anyString(), Matchers.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		baseWikiLinksCKEditorConfigContributor.populateConfigJSONObject(
			jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
			_requestBackedPortletURLFactory);

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject();

		expectedJSONObject.put(
			"filebrowserBrowseUrl", "oneTabItemSelectorPortletURL");
		expectedJSONObject.put("removePlugins", "plugin1");

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	@Test
	public void testItemSelectorURLWhenValidWikiPageAndNode() throws Exception {
		populateInputEditorWikiPageAttributes(1, 1);

		JSONObject originalJSONObject =
			getJSONObjectWithDefaultItemSelectorURL();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		PortletURL itemSelectorPortletURL = Mockito.mock(PortletURL.class);

		Mockito.when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"twoTabsItemSelectorPortletURL"
		);

		Mockito.when(
			_itemSelector.getItemSelectorURL(
				Matchers.any(RequestBackedPortletURLFactory.class),
				Matchers.anyString(), Matchers.any(ItemSelectorCriterion.class),
				Matchers.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		baseWikiLinksCKEditorConfigContributor.populateConfigJSONObject(
			jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
			_requestBackedPortletURLFactory);

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject();

		expectedJSONObject.put(
			"filebrowserBrowseUrl", "twoTabsItemSelectorPortletURL");
		expectedJSONObject.put("removePlugins", "plugin1");

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	@Test
	public void testItemSelectorURLWhenValidWikiPageAndNullNode()
		throws Exception {

		populateInputEditorWikiPageAttributes(1, 0);

		JSONObject originalJSONObject =
			getJSONObjectWithDefaultItemSelectorURL();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		PortletURL itemSelectorPortletURL = Mockito.mock(PortletURL.class);

		Mockito.when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"oneTabItemSelectorPortletURL"
		);

		Mockito.when(
			_itemSelector.getItemSelectorURL(
				Matchers.any(RequestBackedPortletURLFactory.class),
				Matchers.anyString(), Matchers.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		baseWikiLinksCKEditorConfigContributor.populateConfigJSONObject(
			jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
			_requestBackedPortletURLFactory);

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject();

		expectedJSONObject.put(
			"filebrowserBrowseUrl", "oneTabItemSelectorPortletURL");
		expectedJSONObject.put("removePlugins", "plugin1");

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	protected JSONObject getJSONObjectWithDefaultItemSelectorURL()
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("filebrowserBrowseUrl", "defaultItemSelectorPortletURL");

		jsonObject.put("removePlugins", "plugin1");

		return jsonObject;
	}

	protected abstract BaseWikiLinksCKEditorConfigContributor
		getWikiLinksCKEditorConfigContributor();

	protected void populateInputEditorWikiPageAttributes(
		long wikiPageResourcePrimKey, long nodeId) {

		Map<String, String> fileBrowserParamsMap = new HashMap<>();

		fileBrowserParamsMap.put("nodeId", String.valueOf(nodeId));
		fileBrowserParamsMap.put(
			"wikiPageResourcePrimKey", String.valueOf(wikiPageResourcePrimKey));

		_inputEditorTaglibAttributes.put(
			"liferay-ui:input-editor:fileBrowserParams", fileBrowserParamsMap);
	}

	protected BaseWikiLinksCKEditorConfigContributor
		baseWikiLinksCKEditorConfigContributor;

	private final Map<String, Object> _inputEditorTaglibAttributes =
		new HashMap<>();

	@Mock
	private ItemSelector _itemSelector;

	private RequestBackedPortletURLFactory _requestBackedPortletURLFactory;
	private ThemeDisplay _themeDisplay;

}