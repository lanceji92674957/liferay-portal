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

package com.liferay.portal.osgi.debug.reference.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.osgi.reference.spi.UnresolvedStaticReferenceVisitorClient;

/**
 * @author Preston Crary
 */
public class StaticReferenceDependencyUtil {

	public static String scanUnresolvedStaticReferences(
		UnresolvedStaticReferenceVisitorClient
			unresolvedStaticReferenceVisitorClient) {

		StringBundler sb = new StringBundler();

		unresolvedStaticReferenceVisitorClient.visitUnresolvedStaticReference(
			bundle -> {
				sb.append("\nBundle {id: ");
				sb.append(bundle.getBundleId());
				sb.append(", name: ");
				sb.append(bundle.getSymbolicName());
				sb.append(", version: ");
				sb.append(bundle.getVersion());
				sb.append("}.\n");
				sb.append("Is unavailable due to missing required ");
				sb.append("dependencies:");

				return s -> {
					sb.append("\n\t");
					sb.append(s);
				};
			});

		return sb.toString();
	}

	private StaticReferenceDependencyUtil() {
	}

}