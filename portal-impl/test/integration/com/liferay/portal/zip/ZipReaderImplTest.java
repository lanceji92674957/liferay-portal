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

package com.liferay.portal.zip;

import com.liferay.portal.kernel.test.util.DependenciesTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import de.schlichtherle.io.FileInputStream;

import java.io.InputStream;

import java.nio.charset.Charset;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class ZipReaderImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_expectedContent0 = StringUtil.read(
			DependenciesTestUtil.getDependencyAsInputStream(
				ZipReaderImplTest.class, _FILE_PATH_0));
		_expectedContent1 = StringUtil.read(
			DependenciesTestUtil.getDependencyAsInputStream(
				ZipReaderImplTest.class, _FILE_PATH_1));
		_expectedContent2 = StringUtil.read(
			DependenciesTestUtil.getDependencyAsInputStream(
				ZipReaderImplTest.class, _FILE_PATH_2));
		_expectedContent3 = StringUtil.read(
			DependenciesTestUtil.getDependencyAsInputStream(
				ZipReaderImplTest.class, _FILE_PATH_3));
	}

	@Test
	public void testConstructor() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNotNull(zipReader);
		}

		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsFile(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNotNull(zipReader);
		}
	}

	@Test
	public void testGetEntries() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			List<String> entries = zipReader.getEntries();

			Assert.assertEquals(entries.toString(), 5, entries.size());
			Assert.assertEquals(_FILE_PATH_0, entries.get(0));
			Assert.assertEquals(_FILE_PATH_1, entries.get(1));
			Assert.assertEquals(_FILE_PATH_2, entries.get(2));
			Assert.assertEquals(_FILE_PATH_3, entries.get(3));
			Assert.assertEquals(_FILE_PATH_4, entries.get(4));
		}
	}

	@Test
	public void testGetEntryAsByteArray() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			byte[] bytes = zipReader.getEntryAsByteArray(_FILE_PATH_0);

			Assert.assertArrayEquals(_expectedContent0.getBytes(_UTF_8), bytes);

			bytes = zipReader.getEntryAsByteArray(_FILE_PATH_1);

			Assert.assertArrayEquals(_expectedContent1.getBytes(_UTF_8), bytes);

			bytes = zipReader.getEntryAsByteArray(_FILE_PATH_2);

			Assert.assertArrayEquals(_expectedContent2.getBytes(_UTF_8), bytes);

			bytes = zipReader.getEntryAsByteArray(_FILE_PATH_3);

			Assert.assertArrayEquals(_expectedContent3.getBytes(_UTF_8), bytes);
		}
	}

	@Test
	public void testGetEntryAsByteArrayThatDoesNotExist() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsByteArray("foo.txt"));
		}
	}

	@Test
	public void testGetEntryAsByteArrayThatIsADirectory() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsByteArray("1"));
			Assert.assertNull(zipReader.getEntryAsByteArray("/1"));
		}
	}

	@Test
	public void testGetEntryAsByteArrayWithEmptyName() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsByteArray(""));
			Assert.assertNull(zipReader.getEntryAsByteArray(null));
		}
	}

	@Test
	public void testGetEntryAsInputStream() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			InputStream is = zipReader.getEntryAsInputStream(_FILE_PATH_0);

			Assert.assertTrue(is instanceof FileInputStream);

			is = zipReader.getEntryAsInputStream(_FILE_PATH_1);

			Assert.assertTrue(is instanceof FileInputStream);

			is = zipReader.getEntryAsInputStream(_FILE_PATH_2);

			Assert.assertTrue(is instanceof FileInputStream);

			is = zipReader.getEntryAsInputStream(_FILE_PATH_3);

			Assert.assertTrue(is instanceof FileInputStream);
		}
	}

	@Test
	public void testGetEntryAsInputStreamThatDoesNotExist() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsInputStream("foo.txt"));
		}
	}

	@Test
	public void testGetEntryAsInputStreamThatIsADirectory() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsInputStream("1"));
			Assert.assertNull(zipReader.getEntryAsInputStream("/1"));
		}
	}

	@Test
	public void testGetEntryAsInputStreamWithEmptyName() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsInputStream(""));
			Assert.assertNull(zipReader.getEntryAsInputStream(null));
		}
	}

	@Test
	public void testGetEntryAsString() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			String content = zipReader.getEntryAsString(_FILE_PATH_0);

			Assert.assertEquals(_expectedContent0, content);

			content = zipReader.getEntryAsString("/" + _FILE_PATH_0);

			Assert.assertEquals(_expectedContent0, content);

			content = zipReader.getEntryAsString(_FILE_PATH_1);

			Assert.assertEquals(_expectedContent1, content);

			content = zipReader.getEntryAsString(_FILE_PATH_2);

			Assert.assertEquals(_expectedContent2, content);

			content = zipReader.getEntryAsString(_FILE_PATH_3);

			Assert.assertEquals(_expectedContent3, content);
		}
	}

	@Test
	public void testGetEntryAsStringThatDoesNotExist() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsString("foo.txt"));
		}
	}

	@Test
	public void testGetEntryAsStringThatIsADirectory() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsString("1"));
			Assert.assertNull(zipReader.getEntryAsString("/1"));
		}
	}

	@Test
	public void testGetEntryAsStringWithEmptyName() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			Assert.assertNull(zipReader.getEntryAsString(""));
			Assert.assertNull(zipReader.getEntryAsString(null));
		}
	}

	@Test
	public void testGetFolderEntries() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			List<String> entries = zipReader.getFolderEntries("");

			Assert.assertNotNull(entries);
			Assert.assertTrue(entries.isEmpty());

			entries = zipReader.getFolderEntries("/");

			Assert.assertEquals(entries.toString(), 1, entries.size());
			Assert.assertEquals(_FILE_PATH_0, entries.get(0));

			entries = zipReader.getFolderEntries("1");

			Assert.assertEquals(entries.toString(), 2, entries.size());
			Assert.assertEquals(_FILE_PATH_1, entries.get(0));
			Assert.assertEquals(_FILE_PATH_4, entries.get(1));

			entries = zipReader.getFolderEntries("1/2");

			Assert.assertEquals(entries.toString(), 2, entries.size());
			Assert.assertEquals(_FILE_PATH_2, entries.get(0));
			Assert.assertEquals(_FILE_PATH_3, entries.get(1));
		}
	}

	@Test
	public void testGetFolderEntriesThatDoesNotExist() throws Exception {
		try (ZipReader zipReader = new ZipReaderImpl(
				DependenciesTestUtil.getDependencyAsInputStream(
					getClass(), _ZIP_FILE_PATH))) {

			List<String> entries = zipReader.getFolderEntries("foo");

			Assert.assertNotNull(entries);
			Assert.assertTrue(entries.isEmpty());
		}
	}

	private static final String _FILE_PATH_0 = "0.txt";

	private static final String _FILE_PATH_1 = "1/1.txt";

	private static final String _FILE_PATH_2 = "1/2/2.txt";

	private static final String _FILE_PATH_3 = "1/2/3.txt";

	private static final String _FILE_PATH_4 = "1/4.txt";

	private static final Charset _UTF_8 = Charset.forName("UTF-8");

	private static final String _ZIP_FILE_PATH = "file.zip";

	private static String _expectedContent0;
	private static String _expectedContent1;
	private static String _expectedContent2;
	private static String _expectedContent3;

}