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

package com.liferay.dynamic.data.mapping.service;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for DDMStructureVersion. This utility wraps
 * <code>com.liferay.dynamic.data.mapping.service.impl.DDMStructureVersionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureVersionService
 * @generated
 */
@ProviderType
public class DDMStructureVersionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.dynamic.data.mapping.service.impl.DDMStructureVersionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.dynamic.data.mapping.model.DDMStructureVersion
			getLatestStructureVersion(long structureId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getLatestStructureVersion(structureId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.dynamic.data.mapping.model.DDMStructureVersion
			getStructureVersion(long structureVersionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getStructureVersion(structureVersionId);
	}

	public static java.util.List
		<com.liferay.dynamic.data.mapping.model.DDMStructureVersion>
				getStructureVersions(
					long structureId, int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.dynamic.data.mapping.model.
							DDMStructureVersion> orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getStructureVersions(
			structureId, start, end, orderByComparator);
	}

	public static int getStructureVersionsCount(long structureId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getStructureVersionsCount(structureId);
	}

	public static DDMStructureVersionService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<DDMStructureVersionService, DDMStructureVersionService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DDMStructureVersionService.class);

		ServiceTracker<DDMStructureVersionService, DDMStructureVersionService>
			serviceTracker =
				new ServiceTracker
					<DDMStructureVersionService, DDMStructureVersionService>(
						bundle.getBundleContext(),
						DDMStructureVersionService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}