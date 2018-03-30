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

package com.liferay.dynamic.data.lists.exporter;

import java.util.Set;

/**
 * Provides a factory to fetch implementations of the DDL Exporter service. By
 * default, implementations for XML and CSV formats are available, but others
 * can be added as OSGi modules.
 *
 * @author Marcellus Tavares
 * @see    DDLExporter
 */
public interface DDLExporterFactory {

	/**
	 * Returns the available formats that can be used to export record set
	 * records.
	 *
	 * @return the available formats registered in the system
	 */
	public Set<String> getAvailableFormats();

	/**
	 * Returns the DDL Exporter service instance for the format.
	 *
	 * @param  format the format that will be used to export
	 * @return the DDL Exporter instance
	 */
	public DDLExporter getDDLExporter(String format);

}