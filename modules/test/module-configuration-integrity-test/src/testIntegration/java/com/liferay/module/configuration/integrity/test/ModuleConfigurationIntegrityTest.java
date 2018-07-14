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

package com.liferay.module.configuration.integrity.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeInformation;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

/**
 * @author Lance Ji
 */
@RunWith(Arquillian.class)
public class ModuleConfigurationIntegrityTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testModuleConfigurationIntegrity() {
		Bundle currentBundle = FrameworkUtil.getBundle(
			ModuleConfigurationIntegrityTest.class);

		BundleContext bundleContext = currentBundle.getBundleContext();

		Set<String> configurationPids = new HashSet<>();
		Set<String> componentPids = new HashSet<>();

		StringBundler sb = new StringBundler();

		for (Bundle bundle : bundleContext.getBundles()) {
			ExtendedMetaTypeInformation extendedMetaTypeInformation =
				_extendedMetaTypeService.getMetaTypeInformation(bundle);

			String bundleName = bundle.getSymbolicName();

			if (!bundleName.startsWith("com.liferay")) {
				continue;
			}

			Collections.addAll(
				configurationPids,
				extendedMetaTypeInformation.getFactoryPids());
			Collections.addAll(
				configurationPids, extendedMetaTypeInformation.getPids());

			Collection<ComponentDescriptionDTO> componentDescriptionDTOs =
				_serviceComponentRuntime.getComponentDescriptionDTOs(bundle);

			for (ComponentDescriptionDTO componentDescriptionDTO :
					componentDescriptionDTOs) {

				String implementationClass =
					componentDescriptionDTO.implementationClass;

				String[] currentPids = componentDescriptionDTO.configurationPid;

				if (currentPids[0].equals(implementationClass)) {
					continue;
				}

				Collections.addAll(
					componentPids, componentDescriptionDTO.configurationPid);
			}
		}

		for (String pid : configurationPids) {
			if (!componentPids.contains(pid)) {
				sb.append(pid);
				sb.append(StringPool.NEW_LINE);
			}
		}

		Assert.assertTrue(sb.toString(), sb.index() == 0);
	}

	@Inject
	private static ExtendedMetaTypeService _extendedMetaTypeService;

	@Inject
	private static ServiceComponentRuntime _serviceComponentRuntime;

}