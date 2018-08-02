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

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author Lance Ji
 */
public class GeneralProperties extends Properties {

	public GeneralProperties() {
		_safe = false;
	}

	public GeneralProperties(boolean safe) {
		_safe = safe;
	}

	@Override
	public synchronized String toString() {
		StringBundler sb = new StringBundler(4 * size());

		Map<String, String> treeMap = new TreeMap(this);

		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			String value = entry.getValue();

			if (_safe) {
				value = _encode(value);
			}

			sb.append(entry.getKey());
			sb.append(StringPool.EQUAL);
			sb.append(value);
			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	private static String _encode(String value) {
		String encodedValue = StringUtil.replace(
			value, StringPool.RETURN_NEW_LINE, _SAFE_NEWLINE_CHARACTER);

		return StringUtil.replace(
			encodedValue, new char[] {CharPool.NEW_LINE, CharPool.RETURN},
			new String[] {_SAFE_NEWLINE_CHARACTER, _SAFE_NEWLINE_CHARACTER});
	}

	private static final String _SAFE_NEWLINE_CHARACTER =
		"_SAFE_NEWLINE_CHARACTER_";

	private final boolean _safe;

}