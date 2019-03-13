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

package com.liferay.portal.osgi.reference.internal;

import java.lang.reflect.Field;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Preston Crary
 */
public class StaticReferenceServiceTrackerCustomizer
	implements ServiceTrackerCustomizer<Object, Object> {

	public StaticReferenceServiceTrackerCustomizer(Bundle bundle) {
		_bundle = bundle;
	}

	@Override
	public synchronized Object addingService(
		ServiceReference<Object> serviceReference) {

		int index = Collections.binarySearch(
			_serviceReferences, serviceReference, Comparator.reverseOrder());

		if (index >= 0) {
			return null;
		}

		index = -index - 1;

		_serviceReferences.add(index, serviceReference);

		if (_injectedServiceReference == null) {
			_cannotResolve = false;

			_tryInjectService();
		}

		return serviceReference;
	}

	public void addServiceReferences(String className, String fieldName) {
		_classAndFieldNames.add(
			new AbstractMap.SimpleImmutableEntry<>(className, fieldName));
	}

	public boolean cannotResolve() {
		if (_serviceReferences.isEmpty()) {
			return true;
		}

		return _cannotResolve;
	}

	public void close() {
		_serviceTracker.close();
	}

	@Override
	public void modifiedService(
		ServiceReference<Object> serviceReference, Object object) {
	}

	public void open(BundleContext systemBundleContext, String service) {
		if (service.startsWith("(")) {
			try {
				_serviceTracker = new ServiceTracker<>(
					systemBundleContext,
					systemBundleContext.createFilter(service), this);
			}
			catch (InvalidSyntaxException ise) {
				throw new RuntimeException(ise);
			}
		}
		else {
			_serviceTracker = new ServiceTracker<>(
				systemBundleContext, service, this);
		}

		_serviceTracker.open();
	}

	@Override
	public synchronized void removedService(
		ServiceReference<Object> serviceReference, Object object) {

		_serviceReferences.remove(serviceReference);

		if (serviceReference == _injectedServiceReference) {
			_injectedServiceReference = null;

			_tryInjectService();
		}
	}

	private void _tryInjectService() {
		BundleContext bundleContext = _bundle.getBundleContext();

		for (ServiceReference<Object> serviceReference : _serviceReferences) {
			Object service = bundleContext.getService(serviceReference);

			if (service == null) {
				continue;
			}

			_injectedServiceReference = serviceReference;

			for (Map.Entry<String, String> entry : _classAndFieldNames) {
				try {
					Class<?> clazz = _bundle.loadClass(entry.getKey());

					Field field = clazz.getDeclaredField(entry.getValue());

					field.setAccessible(true);

					field.set(null, service);
				}
				catch (ReflectiveOperationException roe) {
					throw new RuntimeException(roe);
				}
			}

			return;
		}

		_cannotResolve = true;
	}

	private final Bundle _bundle;
	private boolean _cannotResolve;
	private final List<Map.Entry<String, String>> _classAndFieldNames =
		new ArrayList<>();
	private ServiceReference<Object> _injectedServiceReference;
	private final List<ServiceReference<Object>> _serviceReferences =
		new ArrayList<>();
	private ServiceTracker<?, ?> _serviceTracker;

}