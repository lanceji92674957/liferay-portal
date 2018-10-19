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

package com.liferay.layout.type.controller.link.to.page.internal.model;

import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypeAccessPolicy;
import com.liferay.portal.kernel.model.impl.ModificationDeniedLayoutTypeAccessPolicyImpl;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pavel Sivanov
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	immediate = true,
	property = "layout.type=" + LayoutConstants.TYPE_LINK_TO_LAYOUT,
	service = LayoutTypeAccessPolicy.class
)
public class LinkToPageLayoutTypeAccessPolicy
	extends ModificationDeniedLayoutTypeAccessPolicyImpl {
}