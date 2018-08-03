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
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Lance Ji
 */
public class GeneralProperties extends HashMap<String, String> {

	public GeneralProperties() {
		_safe = false;
	}

	public GeneralProperties(boolean safe) {
		_safe = safe;
	}

	public void load(String props) throws IOException {
		if (Validator.isNull(props)) {
			return;
		}

		try (UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(new UnsyncStringReader(props))) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				put(line);
			}
		}
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
				value = PropertiesEncoderDecoder.decode(value);
			}

			put(StringUtil.trim(line.substring(0, pos)), value);
		}
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(4 * size());

		Map<String, String> treeMap = new TreeMap(this);

		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			String value = entry.getValue();

			if (value == null) {
				continue;
			}

			if (_safe) {
				value = PropertiesEncoderDecoder.encode(value);
			}

			sb.append(entry.getKey());
			sb.append(StringPool.EQUAL);
			sb.append(value);
			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	private boolean _isComment(String line) {
		if (line.isEmpty() || (line.charAt(0) == CharPool.POUND)) {
			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GeneralProperties.class);

	private final boolean _safe;

}