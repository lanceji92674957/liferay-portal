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

package com.liferay.asset.list.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.asset.list.constants.AssetListEntryTypeConstants;
import com.liferay.asset.list.constants.AssetListFormConstants;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.portal.kernel.model.User;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pavel Savinov
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	property = {
		"screen.navigation.category.order:Integer=30",
		"screen.navigation.entry.order:Integer=30"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class AssetListOrderingScreenNavigationEntry
	extends BaseAssetListScreenNavigationEntry {

	@Override
	public String getCategoryKey() {
		return AssetListFormConstants.ENTRY_KEY_ORDERING;
	}

	@Override
	public String getEntryKey() {
		return AssetListFormConstants.ENTRY_KEY_ORDERING;
	}

	@Override
	public String getJspPath() {
		return "/asset_list/ordering.jsp";
	}

	@Override
	public boolean isVisible(User user, AssetListEntry assetListEntry) {
		if (assetListEntry == null) {
			return false;
		}

		if (assetListEntry.getType() ==
				AssetListEntryTypeConstants.TYPE_DYNAMIC) {

			return true;
		}

		return false;
	}

}