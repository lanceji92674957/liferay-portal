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

package com.liferay.apio.architect.impl.pagination;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import com.liferay.apio.architect.pagination.Page;
import com.liferay.portal.kernel.test.util.MockHelperUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class PageTypeTest {

	@Before
	public void setUp() throws Exception {
		MockHelperUtil.setMethodAlwaysReturnExpected(_page, "getPageNumber", 3);

		MockHelperUtil.setMethodAlwaysReturnExpected(
			_page, "getLastPageNumber", 5);
	}

	@Test
	public void testCallingPageNumberOnCurrentReturnsCurrent() {
		Integer pageNumber = PageType.CURRENT.getPageNumber(_page);

		assertThat(pageNumber, is(3));
	}

	@Test
	public void testCallingPageNumberOnFirstReturnsOne() {
		Integer pageNumber = PageType.FIRST.getPageNumber(_page);

		assertThat(pageNumber, is(1));
	}

	@Test
	public void testCallingPageNumberOnLastReturnsLast() {
		Integer pageNumber = PageType.LAST.getPageNumber(_page);

		assertThat(pageNumber, is(5));
	}

	@Test
	public void testCallingPageNumberOnNextReturnsNext() {
		Integer pageNumber = PageType.NEXT.getPageNumber(_page);

		assertThat(pageNumber, is(4));
	}

	@Test
	public void testCallingPageNumberOnNextWithLastPageReturnsLast()
		throws Exception {

		MockHelperUtil.setMethodAlwaysReturnExpected(
			_page, "getLastPageNumber", 3);

		Integer pageNumber = PageType.NEXT.getPageNumber(_page);

		assertThat(pageNumber, is(3));
	}

	@Test
	public void testCallingPageNumberOnPreviousReturnsPrevious() {
		Integer pageNumber = PageType.PREVIOUS.getPageNumber(_page);

		assertThat(pageNumber, is(2));
	}

	@Test
	public void testCallingPageNumberOnPreviousWithPageOneReturnsOne()
		throws Exception {

		MockHelperUtil.setMethodAlwaysReturnExpected(_page, "getPageNumber", 1);

		Integer pageNumber = PageType.PREVIOUS.getPageNumber(_page);

		assertThat(pageNumber, is(1));
	}

	private final Page _page = MockHelperUtil.initMock(Page.class);

}