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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author Preston Crary
 */
public class StaticReferenceResolver {

	public StaticReferenceResolver(Bundle bundle) {
		_bundle = bundle;
	}

	public void close() {
		for (StaticReferenceServiceTrackerCustomizer
				bundleServiceStaticReferences :
					_staticReferenceServiceTrackerCustomizers.values()) {

			bundleServiceStaticReferences.close();
		}
	}

	public Bundle getBundle() {
		return _bundle;
	}

	public boolean isTryResolve() {
		for (StaticReferenceServiceTrackerCustomizer
				staticReferenceServiceTrackerCustomizer :
					_staticReferenceServiceTrackerCustomizers.values()) {

			if (staticReferenceServiceTrackerCustomizer.cannotResolve()) {
				return false;
			}
		}

		return true;
	}

	public void open(BundleContext systemBundleContext) {
		for (Map.Entry<String, StaticReferenceServiceTrackerCustomizer> entry :
				_staticReferenceServiceTrackerCustomizers.entrySet()) {

			String service = entry.getKey();
			StaticReferenceServiceTrackerCustomizer
				staticReferenceServiceTrackerCustomizer = entry.getValue();

			staticReferenceServiceTrackerCustomizer.open(
				systemBundleContext, service);
		}
	}

	public void registerStaticReference(
		String className, String fieldName, String service) {

		_staticReferenceServiceTrackerCustomizers.compute(
			service,
			(key, staticReferenceServiceTrackerCustomizer) -> {
				if (staticReferenceServiceTrackerCustomizer == null) {
					staticReferenceServiceTrackerCustomizer =
						new StaticReferenceServiceTrackerCustomizer(_bundle);
				}

				staticReferenceServiceTrackerCustomizer.addServiceReferences(
					className, fieldName);

				return staticReferenceServiceTrackerCustomizer;
			});
	}

	public void visitUnresolvedStaticReferences(Consumer<String> consumer) {
		for (Map.Entry<String, StaticReferenceServiceTrackerCustomizer> entry :
				_staticReferenceServiceTrackerCustomizers.entrySet()) {

			StaticReferenceServiceTrackerCustomizer
				staticReferenceServiceTrackerCustomizer = entry.getValue();

			if (staticReferenceServiceTrackerCustomizer.cannotResolve()) {
				consumer.accept(entry.getKey());
			}
		}
	}

	private final Bundle _bundle;
	private final Map<String, StaticReferenceServiceTrackerCustomizer>
		_staticReferenceServiceTrackerCustomizers = new HashMap<>();

}