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

package com.liferay.application.list.internal;

import com.liferay.application.list.GroupProvider;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelAppRegistry;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.util.PanelCategoryServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.kernel.util.PrefsProps;

import java.io.Serializable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lance Ji
 */
@Component(immediate = true, service = PanelAppRegistry.class)
public class PanelAppRegistryImpl implements PanelAppRegistry {

	@Override
	public PanelApp getFirstPanelApp(
		String parentPanelCategoryKey, PermissionChecker permissionChecker,
		Group group) {

		List<PanelApp> panelApps = getPanelApps(parentPanelCategoryKey);

		for (PanelApp panelApp : panelApps) {
			try {
				if (panelApp.isShow(permissionChecker, group)) {
					return panelApp;
				}
			}
			catch (PortalException pe) {
				_log.error(pe, pe);
			}
		}

		return null;
	}

	@Override
	public List<PanelApp> getPanelApps(PanelCategory parentPanelCategory) {
		return getPanelApps(parentPanelCategory.getKey());
	}

	@Override
	public List<PanelApp> getPanelApps(
		PanelCategory parentPanelCategory,
		final PermissionChecker permissionChecker, final Group group) {

		return getPanelApps(
			parentPanelCategory.getKey(), permissionChecker, group);
	}

	@Override
	public List<PanelApp> getPanelApps(String parentPanelCategoryKey) {
		List<PanelApp> panelApps = _serviceTrackerMap.getService(
			parentPanelCategoryKey);

		if (panelApps == null) {
			return Collections.emptyList();
		}

		return panelApps;
	}

	@Override
	public List<PanelApp> getPanelApps(
		String parentPanelCategoryKey,
		final PermissionChecker permissionChecker, final Group group) {

		List<PanelApp> panelApps = getPanelApps(parentPanelCategoryKey);

		if (panelApps.isEmpty()) {
			return panelApps;
		}

		return ListUtil.filter(
			panelApps,
			new PredicateFilter<PanelApp>() {

				@Override
				public boolean filter(PanelApp panelApp) {
					try {
						return panelApp.isShow(permissionChecker, group);
					}
					catch (PortalException pe) {
						_log.error(pe, pe);
					}

					return false;
				}

			});
	}

	@Override
	public int getPanelAppsNotificationsCount(
		String parentPanelCategoryKey, PermissionChecker permissionChecker,
		Group group, User user) {

		int count = 0;

		for (PanelApp panelApp : getPanelApps(parentPanelCategoryKey)) {
			int notificationsCount = panelApp.getNotificationsCount(user);

			try {
				if ((notificationsCount > 0) &&
					panelApp.isShow(permissionChecker, group)) {

					count += notificationsCount;
				}
			}
			catch (PortalException pe) {
				_log.error(pe, pe);
			}
		}

		return count;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext, PanelApp.class, "(panel.category.key=*)",
			new PanelCategoryServiceReferenceMapper(),
			new PanelAppOrderComparator(),
			new PanelAppsServiceTrackerMapListener());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	@Reference(unbind = "-")
	protected void setGroupProvider(GroupProvider groupProvider) {
		this.groupProvider = groupProvider;
	}

	@Reference(unbind = "-")
	protected void setPortletLocalService(
		PortletLocalService portletLocalService) {

		this.portletLocalService = portletLocalService;
	}

	protected GroupProvider groupProvider;
	protected PortletLocalService portletLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		PanelAppRegistryImpl.class);

	@Reference
	private PortletPreferencesFactory _portletPreferencesFactory;

	@Reference
	private PrefsProps _prefsProps;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	private ServiceTrackerMap<String, List<PanelApp>> _serviceTrackerMap;

	private class PanelAppOrderComparator
		implements Comparator<ServiceReference<PanelApp>>, Serializable {

		@Override
		public int compare(
			ServiceReference serviceReference1,
			ServiceReference serviceReference2) {

			if (serviceReference1 == null) {
				if (serviceReference2 == null) {
					return 0;
				}
				else {
					return 1;
				}
			}
			else if (serviceReference2 == null) {
				return -1;
			}

			Object propertyValue1 = serviceReference1.getProperty(
				"panel.app.order");
			Object propertyValue2 = serviceReference2.getProperty(
				"panel.app.order");

			if (propertyValue1 == null) {
				if (propertyValue2 == null) {
					return 0;
				}
				else {
					return 1;
				}
			}
			else if (propertyValue2 == null) {
				return -1;
			}

			if (!(propertyValue2 instanceof Comparable)) {
				return -serviceReference2.compareTo(serviceReference1);
			}

			Comparable<Object> propertyValueComparable2 =
				(Comparable<Object>)propertyValue2;

			return -propertyValueComparable2.compareTo(propertyValue1);
		}

	}

	private class PanelAppsServiceTrackerMapListener
		implements ServiceTrackerMapListener<String, PanelApp, List<PanelApp>> {

		@Override
		public void keyEmitted(
			ServiceTrackerMap<String, List<PanelApp>> serviceTrackerMap,
			String panelCategoryKey, PanelApp panelApp,
			List<PanelApp> panelApps) {

			panelApp.setGroupProvider(groupProvider);

			Portlet portlet = portletLocalService.getPortletById(
				panelApp.getPortletId());

			if (portlet != null) {
				portlet.setControlPanelEntryCategory(panelCategoryKey);

				panelApp.setPortlet(portlet);
			}
			else if (_log.isDebugEnabled()) {
				_log.debug("Unable to get portlet " + panelApp.getPortletId());
			}
		}

		@Override
		public void keyRemoved(
			ServiceTrackerMap<String, List<PanelApp>> serviceTrackerMap,
			String panelCategoryKey, PanelApp panelApp,
			List<PanelApp> panelApps) {
		}

	}

}