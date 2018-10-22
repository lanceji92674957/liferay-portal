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

package com.liferay.portal.kernel.servlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.ProxyTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Tomas Polesovsky
 */
public class ServletResponseUtilRangeTest {

	@Before
	public void setUp() {
		PropsUtil.setProps(
			ProxyTestUtil.getProxy(
				Props.class,
				ProxyTestUtil.getProxyMethod(
					"get",
					args -> {
						if ((args.length == 1) &&
							PropsKeys.WEB_SERVER_SERVLET_MAX_RANGE_FIELDS.
								equals(args[0])) {

							return "10";
						}

						return "";
					})));
	}

	@Test
	public void testGetMultipleRanges() throws IOException {
		setUpRange("bytes=1-3,3-8,9-11,12-12,30-");

		List<Range> ranges = ServletResponseUtil.getRanges(
			_request, null, 1000);

		Assert.assertEquals(ranges.toString(), 5, ranges.size());
		assertRange(ranges.get(0), 1, 3, 3);
		assertRange(ranges.get(1), 3, 8, 6);
		assertRange(ranges.get(2), 9, 11, 3);
		assertRange(ranges.get(3), 12, 12, 1);
		assertRange(ranges.get(4), 30, 999, 970);
	}

	@Test
	public void testGetRangesPerSpec() throws IOException {

		// https://tools.ietf.org/html/rfc7233#section-2.1

		long length = 10000;

		// The final 500 bytes (byte offsets 9500-9999, inclusive): bytes=-500
		// or bytes=9500-

		setUpRange("bytes=-500");

		List<Range> ranges = ServletResponseUtil.getRanges(
			_request, null, length);

		Assert.assertEquals(ranges.toString(), 1, ranges.size());
		assertRange(ranges.get(0), 9500, 9999, 500);

		setUpRange("bytes=9500-");

		ranges = ServletResponseUtil.getRanges(_request, null, length);

		Assert.assertEquals(ranges.toString(), 1, ranges.size());
		assertRange(ranges.get(0), 9500, 9999, 500);

		// The first and last bytes only (bytes 0 and 9999): bytes=0-0,-1

		setUpRange("bytes=0-0,-1");

		ranges = ServletResponseUtil.getRanges(_request, null, length);

		Assert.assertEquals(ranges.toString(), 2, ranges.size());
		assertRange(ranges.get(0), 0, 0, 1);
		assertRange(ranges.get(1), 9999, 9999, 1);

		// Other valid (but not canonical) specifications of the second 500
		// bytes (byte offsets 500-999, inclusive): bytes=500-600,601-999 or
		// bytes=500-700,601-999

		setUpRange("bytes=500-600,601-999");

		ranges = ServletResponseUtil.getRanges(_request, null, length);

		Assert.assertEquals(ranges.toString(), 2, ranges.size());
		assertRange(ranges.get(0), 500, 600, 101);
		assertRange(ranges.get(1), 601, 999, 399);

		setUpRange("bytes=500-700,601-999");

		ranges = ServletResponseUtil.getRanges(_request, null, length);

		Assert.assertEquals(ranges.toString(), 2, ranges.size());
		assertRange(ranges.get(0), 500, 700, 201);
		assertRange(ranges.get(1), 601, 999, 399);
	}

	@Test
	public void testGetRangesSimple() throws IOException {
		setUpRange("bytes=0-999");

		List<Range> ranges = ServletResponseUtil.getRanges(
			_request, null, 1000);

		Assert.assertEquals(ranges.toString(), 1, ranges.size());
		assertRange(ranges.get(0), 0, 999, 1000);
	}

	@Test
	public void testWriteWithRanges() throws IOException {
		ReflectionTestUtil.setFieldValue(
			FileUtil.class, "_file",
			ProxyTestUtil.getProxy(
				com.liferay.portal.kernel.util.File.class,
				ProxyTestUtil.getProxyMethod(
					"createTempFile",
					args -> new File(
						StringBundler.concat(
							SystemProperties.TMP_DIR, StringPool.SLASH,
							GetterUtil.getString(System.currentTimeMillis())))),
				ProxyTestUtil.getProxyMethod(
					"delete", args -> ((File)args[0]).delete())));

		byte[] content = new byte[1000];

		Arrays.fill(content, (byte)48);

		testWriteWith(new ByteArrayInputStream(content), content);

		File tempFile = FileUtil.createTempFile();

		try {
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(content);
			}

			testWriteWith(new FileInputStream(tempFile), content);
		}
		finally {
			tempFile.delete();
		}

		testWriteWith(
			new BufferedInputStream(new ByteArrayInputStream(content)),
			content);
	}

	protected void assertRange(Range range, long start, long end, long length) {
		Assert.assertEquals(range.getStart(), start);
		Assert.assertEquals(range.getEnd(), end);
		Assert.assertEquals(range.getLength(), length);
	}

	protected void setUpRange(String rangeHeader) {
		_request = ProxyTestUtil.getProxy(
			HttpServletRequest.class,
			ProxyTestUtil.getProxyMethod(
				"getHeader",
				args -> {
					if (args[0] == HttpHeaders.RANGE) {
						return rangeHeader;
					}

					return "";
				}));
	}

	protected void testWriteWith(InputStream inputStream, byte[] content)
		throws IOException {

		setUpRange("bytes=0-9,980-989,980-999,990-999");

		List<Range> ranges = ServletResponseUtil.getRanges(
			_request, null, content.length);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		mockHttpServletResponse.setCharacterEncoding(StringPool.UTF8);

		ServletResponseUtil.write(
			_request, mockHttpServletResponse, "filename.txt", ranges,
			inputStream, content.length, "text/plain");

		String contentType = mockHttpServletResponse.getContentType();

		Assert.assertTrue(
			contentType.startsWith(_CONTENT_TYPE_BOUNDARY_PREFACE));

		String boundary = contentType.substring(
			_CONTENT_TYPE_BOUNDARY_PREFACE.length());

		String responseBody = mockHttpServletResponse.getContentAsString();

		Assert.assertTrue(
			responseBody.startsWith("\r\n--" + boundary + "\r\n"));
		Assert.assertTrue(responseBody.endsWith("--" + boundary + "--\r\n"));

		String[] responseBodies = StringUtil.split(responseBody, boundary);

		Assert.assertEquals(
			Arrays.toString(responseBodies), ranges.size() + 2,
			responseBodies.length);
		Assert.assertEquals(StringPool.DOUBLE_DASH, responseBodies[0]);
		Assert.assertEquals(
			StringPool.DOUBLE_DASH, responseBodies[ranges.size() + 1]);

		for (int i = 0; i < ranges.size(); i++) {
			Range range = ranges.get(i);

			String[] lines = StringUtil.split(
				responseBodies[i + 1], StringPool.RETURN_NEW_LINE);

			Assert.assertEquals("Content-Type: text/plain", lines[0]);
			Assert.assertEquals(
				"Content-Range: " + range.getContentRange(), lines[1]);
			Assert.assertTrue(Validator.isNull(lines[2]));

			int start = (int)range.getStart();
			int end = (int)range.getEnd();

			byte[] bytes = ArrayUtil.subset(content, start, end + 1);

			Assert.assertArrayEquals(bytes, lines[3].getBytes("UTF-8"));

			Assert.assertEquals(StringPool.DOUBLE_DASH, lines[4]);
		}
	}

	private static final String _CONTENT_TYPE_BOUNDARY_PREFACE =
		"multipart/byteranges; boundary=";

	private HttpServletRequest _request;

}