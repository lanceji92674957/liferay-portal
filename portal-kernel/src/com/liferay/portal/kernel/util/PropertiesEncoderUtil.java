/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Lance Ji
 */
public class PropertiesEncoderUtil {

	private static String _decode(String value) {
		return StringUtil.replace(
			value, _SAFE_NEWLINE_CHARACTER, StringPool.NEW_LINE);
	}

	private static String _encode(String value) {
		String encodedValue = StringUtil.replace(
			value, StringPool.RETURN_NEW_LINE, _SAFE_NEWLINE_CHARACTER);

		return StringUtil.replace(
			encodedValue, new char[] {CharPool.NEW_LINE, CharPool.RETURN},
			new String[] {_SAFE_NEWLINE_CHARACTER, _SAFE_NEWLINE_CHARACTER});
	}

	public static String getPropertiesString(Map<String, String> properties, boolean safe) {
		StringBundler sb = new StringBundler(4 * properties.size());

		Map<String, String> treeMap = new TreeMap<>(this);

		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			String value = entry.getValue();

			if (Validator.isNull(value)) {
				continue;
			}

			if (safe) {
				value = _encode(value);
			}

			sb.append(entry.getKey());
			sb.append(StringPool.EQUAL);
			sb.append(value);
			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	public void put(String line) {
		line = line.trim();

		if (_isComment(line)) {
			return;
		}

		int pos = line.indexOf(CharPool.EQUAL);

		if (pos == -1) {
			_log.error("Invalid property on line " + line);
		}
		else {
			String value = StringUtil.trim(line.substring(pos + 1));

			if (_safe) {
				value = _decode(value);
			}

			setProperty(StringUtil.trim(line.substring(0, pos)), value);
		}
	}
	
	private boolean _isComment(String line) {
		if (line.isEmpty() || (line.charAt(0) == CharPool.POUND)) {
			return true;
		}

		return false;
	}
	private static final String _SAFE_NEWLINE_CHARACTER =
		"_SAFE_NEWLINE_CHARACTER_";

}