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

import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.osgi.reference.spi.asm.StaticReferenceASMParserUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import java.net.URL;

import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * @author Preston Crary
 */
public class StaticReferenceBundleTrackerCustomizer
	implements BundleTrackerCustomizer<StaticReferenceResolver> {

	public StaticReferenceBundleTrackerCustomizer(
		FrameworkWiring frameworkWiring, BundleContext bundleContext) {

		_frameworkWiring = frameworkWiring;
		_bundleContext = bundleContext;
	}

	@Override
	public StaticReferenceResolver addingBundle(
		Bundle bundle, BundleEvent bundleEvent) {

		Dictionary<String, String> headers = bundle.getHeaders(
			StringPool.BLANK);

		List<String> staticReferenceClasses = StringUtil.split(
			headers.get("Liferay-Static-Reference-Classes"));

		if (staticReferenceClasses.isEmpty()) {
			return null;
		}

		if ((bundleEvent == null) ||
			(bundleEvent.getType() == BundleEvent.STARTING)) {

			_frameworkWiring.refreshBundles(Collections.singleton(bundle));
		}

		StaticReferenceResolver staticReferenceResolver =
			new StaticReferenceResolver(_frameworkWiring, bundle);

		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
			new UnsyncByteArrayOutputStream();

		for (String staticReferenceClass : staticReferenceClasses) {
			URL url = bundle.getResource(
				staticReferenceClass.replace(CharPool.PERIOD, CharPool.SLASH) +
					".class");

			if (url == null) {
				continue;
			}

			try (InputStream inputStream = url.openStream()) {
				StreamUtil.transfer(inputStream, unsyncByteArrayOutputStream);

				StaticReferenceASMParserUtil.parseStaticReferences(
					unsyncByteArrayOutputStream.toByteArray(),
					(fieldName, service) ->
						staticReferenceResolver.registerStaticReference(
							staticReferenceClass, fieldName, service));
			}
			catch (IOException ioe) {
				throw new UncheckedIOException(ioe);
			}

			unsyncByteArrayOutputStream.reset();
		}

		StaticReferenceResolverUtil.setStaticReferenceResolver(
			staticReferenceResolver);

		staticReferenceResolver.open(_bundleContext);

		return staticReferenceResolver;
	}

	@Override
	public void modifiedBundle(
		Bundle bundle, BundleEvent bundleEvent,
		StaticReferenceResolver staticReferenceResolver) {

		if (bundleEvent.getType() == BundleEvent.STARTING) {
			staticReferenceResolver.tryResolve();
		}
	}

	@Override
	public void removedBundle(
		Bundle bundle, BundleEvent bundleEvent,
		StaticReferenceResolver staticReferenceResolver) {

		staticReferenceResolver.close();
	}

	private final BundleContext _bundleContext;
	private final FrameworkWiring _frameworkWiring;

}