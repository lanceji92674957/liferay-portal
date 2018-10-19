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

package com.liferay.sync.web.internal.upgrade;

import com.liferay.portal.kernel.upgrade.BaseUpgradePortletId;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.sync.constants.SyncAdminPortletKeys;
import com.liferay.sync.constants.SyncPortletKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shinn Lok
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	immediate = true,
	service = {SyncWebUpgrade.class, UpgradeStepRegistrator.class}
)
public class SyncWebUpgrade implements UpgradeStepRegistrator {

	@Override
	@SuppressWarnings("deprecation")
	public void register(Registry registry) {
		registry.register(
			"0.0.0", "1.0.0",
			new BaseUpgradePortletId() {

				@Override
				protected String[][] getRenamePortletIdsArray() {
					return new String[][] {
						{
							SyncAdminPortletKeys.SYNC_ADMIN_PORTLET,
							SyncPortletKeys.SYNC_ADMIN_PORTLET
						},
						{
							SyncAdminPortletKeys.SYNC_DEVICES_PORTLET,
							SyncPortletKeys.SYNC_DEVICES_PORTLET
						}
					};
				}

			});
	}

}