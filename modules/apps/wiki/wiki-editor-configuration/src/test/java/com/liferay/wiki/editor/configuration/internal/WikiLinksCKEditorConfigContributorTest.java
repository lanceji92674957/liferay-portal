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

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.wiki.item.selector.WikiPageURLItemSelectorReturnType;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public class WikiLinksCKEditorConfigContributorTest
	extends BaseWikiLinksCKEditorConfigContributorTestCase {

	@Test
	public void testgetItemSelectorReturnType() {
		ItemSelectorReturnType itemSelectorReturnType =
			baseWikiLinksCKEditorConfigContributor.getItemSelectorReturnType();

		Assert.assertTrue(
			itemSelectorReturnType instanceof
				WikiPageURLItemSelectorReturnType);
	}

	@Override
	protected BaseWikiLinksCKEditorConfigContributor
		getWikiLinksCKEditorConfigContributor() {

		return new WikiLinksCKEditorConfigContributor();
	}

}