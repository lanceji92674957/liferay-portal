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

package com.liferay.site.util;

import com.liferay.portal.kernel.model.Group;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lance Ji
 */
public interface RecentGroupManager {

	public void addRecentGroup(HttpServletRequest request, Group group);

	public void addRecentGroup(HttpServletRequest request, long groupId);

	public List<Group> getRecentGroups(HttpServletRequest request);

}