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

package com.liferay.portal.osgi.debug.reference.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Preston Crary
 */
@ExtendedObjectClassDefinition(category = "module-container")
@Meta.OCD(
	id = "com.liferay.portal.osgi.debug.reference.internal.configuration.UnsatisfiedStaticReferenceScannerConfiguration",
	localization = "content/Language",
	name = "unsatisfied-static-reference-configuration-name"
)
public interface UnsatisfiedStaticReferenceScannerConfiguration {

	@Meta.AD(
		deflt = "-1",
		description = "unsatisfied-static-reference-scanning-interval-help",
		name = "unsatisfied-static-reference-scanning-interval",
		required = false
	)
	public int unsatisfiedStaticReferenceScanningInterval();

}