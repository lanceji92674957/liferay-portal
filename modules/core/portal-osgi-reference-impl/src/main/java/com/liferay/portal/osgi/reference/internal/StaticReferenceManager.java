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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.osgi.reference.spi.UnresolvedStaticReferenceVisitorClient;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

import org.eclipse.osgi.internal.hookregistry.ActivatorHookFactory;
import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.internal.hookregistry.HookRegistry;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.resolver.ResolverHook;
import org.osgi.framework.hooks.resolver.ResolverHookFactory;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Preston Crary
 */
public class StaticReferenceManager
	implements ActivatorHookFactory, BundleActivator, HookConfigurator,
			   ResolverHook, ResolverHookFactory {

	@Override
	public void addHooks(HookRegistry hookRegistry) {
		hookRegistry.addActivatorHookFactory(this);
	}

	@Override
	public ResolverHook begin(Collection<BundleRevision> bundleRevisions) {
		return this;
	}

	@Override
	public BundleActivator createActivator() {
		return this;
	}

	@Override
	public void end() {
	}

	@Override
	public void filterMatches(
		BundleRequirement requirement,
		Collection<BundleCapability> bundleCapabilities) {
	}

	@Override
	public void filterResolvable(Collection<BundleRevision> bundleRevisions) {
		Iterator<BundleRevision> iterator = bundleRevisions.iterator();

		while (iterator.hasNext()) {
			BundleRevision bundleRevision = iterator.next();

			Bundle bundle = bundleRevision.getBundle();

			Dictionary<String, String> headers = bundle.getHeaders(
				StringPool.BLANK);

			if ((headers.get("Liferay-Static-Reference-Classes") != null) &&
				!StaticReferenceResolverUtil.isTryResolve(
					bundleRevision.getBundle())) {

				iterator.remove();
			}
		}
	}

	@Override
	public void filterSingletonCollisions(
		BundleCapability singleton,
		Collection<BundleCapability> bundleCapabilities) {
	}

	@Override
	public void start(BundleContext bundleContext) {
		Bundle bundle = bundleContext.getBundle();

		FrameworkWiring frameworkWiring = bundle.adapt(FrameworkWiring.class);

		_bundleTracker = new BundleTracker<>(
			bundleContext, ~Bundle.UNINSTALLED,
			new StaticReferenceBundleTrackerCustomizer(
				frameworkWiring, bundleContext));

		_bundleTracker.open();

		_resolverHookFactoryServiceRegistration = bundleContext.registerService(
			ResolverHookFactory.class, this, null);

		_unresolvedStaticReferenceVisitorClientServiceRegistration =
			bundleContext.registerService(
				UnresolvedStaticReferenceVisitorClient.class,
				new UnresolvedStaticReferenceVisitorClientImpl(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		_bundleTracker.close();

		_resolverHookFactoryServiceRegistration.unregister();

		_unresolvedStaticReferenceVisitorClientServiceRegistration.unregister();
	}

	private BundleTracker<?> _bundleTracker;
	private ServiceRegistration<ResolverHookFactory>
		_resolverHookFactoryServiceRegistration;
	private ServiceRegistration<UnresolvedStaticReferenceVisitorClient>
		_unresolvedStaticReferenceVisitorClientServiceRegistration;

}