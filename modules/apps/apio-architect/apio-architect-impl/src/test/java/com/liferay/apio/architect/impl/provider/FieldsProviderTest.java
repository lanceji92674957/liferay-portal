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

package com.liferay.apio.architect.impl.provider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.liferay.apio.architect.impl.response.control.Fields;
import com.liferay.portal.kernel.test.util.MockHelperUtil;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class FieldsProviderTest {

	@Test
	public void testFieldsProviderReturnsAlwaysTrueIfEmptyFields()
		throws Exception {

		Predicate<String> predicate = _getPredicate("");

		assertThat(predicate.test("alternateName"), is(true));
		assertThat(predicate.test("givenName"), is(true));
		assertThat(predicate.test("randomField"), is(true));
	}

	@Test
	public void testFieldsProviderReturnsAlwaysTrueIfInvalidParam()
		throws Exception {

		Predicate<String> predicate = _getPredicate(
			"description", "familyName", "givenName");

		assertThat(predicate.test("alternateName"), is(true));
		assertThat(predicate.test("description"), is(true));
		assertThat(predicate.test("givenName"), is(true));
	}

	@Test
	public void testFieldsProviderReturnValidFields() throws Exception {
		Predicate<String> predicate = _getPredicate("familyName,givenName");

		assertThat(predicate.test("alternateName"), is(false));
		assertThat(predicate.test("givenName"), is(true));
	}

	private Predicate<String> _getPredicate(String... personFields)
		throws Exception {

		FieldsProvider fieldsProvider = new FieldsProvider();

		HttpServletRequest httpServletRequest = MockHelperUtil.initMock(
			HttpServletRequest.class);

		Map<String, String[]> parameterMap = Collections.singletonMap(
			"fields[Person]", personFields);

		MockHelperUtil.setMethodAlwaysReturnExpected(
			httpServletRequest, "getParameterMap", parameterMap);

		Fields fields = fieldsProvider.createContext(httpServletRequest);

		return fields.apply(Collections.singletonList("Person"));
	}

}