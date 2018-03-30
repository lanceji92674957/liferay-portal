/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.internal.data.provider;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContextContributor;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;

import java.util.List;
import java.util.Set;

/**
 * @author Lance Ji
 */
public class DDMDataProviderTrackerImpl implements DDMDataProviderTracker {
	@Override
	public DDMDataProvider getDDMDataProvider(
		String type) {
		return null;
	}

	@Override
	public DDMDataProvider getDDMDataProviderByInstanceId(
		String instanceId) {
		return null;
	}

	@Override
	public List<DDMDataProviderContextContributor> getDDMDataProviderContextContributors(
		String type) {
		return null;
	}

	@Override
	public Set<String> getDDMDataProviderTypes() {
		return null;
	}
}
