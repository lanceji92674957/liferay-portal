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

package com.liferay.portal.search.web.internal.facet.display.context;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.util.MockHelperUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.web.internal.facet.display.builder.ScopeSearchFacetDisplayBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author André de Oliveira
 */
public class ScopeSearchFacetDisplayContextTest {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		MockHelperUtil.setMethodAlwaysReturnExpected(
			_facet, "getFacetCollector", _facetCollector);
	}

	@Test
	public void testEmptySearchResults() throws Exception {
		String parameterValue = "0";

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<ScopeSearchFacetTermDisplayContext>
			scopeSearchFacetTermDisplayContexts =
				scopeSearchFacetDisplayContext.getTermDisplayContexts();

		Assert.assertEquals(
			scopeSearchFacetTermDisplayContexts.toString(), 0,
			scopeSearchFacetTermDisplayContexts.size());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testEmptySearchResultsWithPreviousSelection() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		String name = RandomTestUtil.randomString();

		addGroup(groupId, name);

		String parameterValue = String.valueOf(groupId);

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<ScopeSearchFacetTermDisplayContext>
			scopeSearchFacetTermDisplayContexts =
				scopeSearchFacetDisplayContext.getTermDisplayContexts();

		Assert.assertEquals(
			scopeSearchFacetTermDisplayContexts.toString(), 1,
			scopeSearchFacetTermDisplayContexts.size());

		ScopeSearchFacetTermDisplayContext scopeSearchFacetTermDisplayContext =
			scopeSearchFacetTermDisplayContexts.get(0);

		Assert.assertEquals(0, scopeSearchFacetTermDisplayContext.getCount());
		Assert.assertEquals(
			name, scopeSearchFacetTermDisplayContext.getDescriptiveName());
		Assert.assertEquals(
			groupId, scopeSearchFacetTermDisplayContext.getGroupId());
		Assert.assertTrue(scopeSearchFacetTermDisplayContext.isSelected());
		Assert.assertTrue(scopeSearchFacetTermDisplayContext.isShowCount());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTerm() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		String name = RandomTestUtil.randomString();

		addGroup(groupId, name);

		int count = RandomTestUtil.randomInt();

		setUpOneTermCollector(groupId, count);

		String parameterValue = "0";

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<ScopeSearchFacetTermDisplayContext>
			scopeSearchFacetTermDisplayContexts =
				scopeSearchFacetDisplayContext.getTermDisplayContexts();

		Assert.assertEquals(
			scopeSearchFacetTermDisplayContexts.toString(), 1,
			scopeSearchFacetTermDisplayContexts.size());

		ScopeSearchFacetTermDisplayContext scopeSearchFacetTermDisplayContext =
			scopeSearchFacetTermDisplayContexts.get(0);

		Assert.assertEquals(
			count, scopeSearchFacetTermDisplayContext.getCount());
		Assert.assertEquals(
			name, scopeSearchFacetTermDisplayContext.getDescriptiveName());
		Assert.assertEquals(
			groupId, scopeSearchFacetTermDisplayContext.getGroupId());
		Assert.assertFalse(scopeSearchFacetTermDisplayContext.isSelected());
		Assert.assertTrue(scopeSearchFacetTermDisplayContext.isShowCount());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTermWithPreviousSelection() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		String name = RandomTestUtil.randomString();

		addGroup(groupId, name);

		int count = RandomTestUtil.randomInt();

		setUpOneTermCollector(groupId, count);

		String parameterValue = String.valueOf(groupId);

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<ScopeSearchFacetTermDisplayContext>
			scopeSearchFacetTermDisplayContexts =
				scopeSearchFacetDisplayContext.getTermDisplayContexts();

		Assert.assertEquals(
			scopeSearchFacetTermDisplayContexts.toString(), 1,
			scopeSearchFacetTermDisplayContexts.size());

		ScopeSearchFacetTermDisplayContext scopeSearchFacetTermDisplayContext =
			scopeSearchFacetTermDisplayContexts.get(0);

		Assert.assertEquals(
			count, scopeSearchFacetTermDisplayContext.getCount());
		Assert.assertEquals(
			name, scopeSearchFacetTermDisplayContext.getDescriptiveName());
		Assert.assertEquals(
			groupId, scopeSearchFacetTermDisplayContext.getGroupId());
		Assert.assertTrue(scopeSearchFacetTermDisplayContext.isSelected());
		Assert.assertTrue(scopeSearchFacetTermDisplayContext.isShowCount());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	protected void addGroup(long groupId, String name) throws Exception {
		Mockito.doReturn(
			createGroup(groupId, name)
		).when(
			_groupLocalService
		).fetchGroup(
			groupId
		);
	}

	protected ScopeSearchFacetDisplayContext createDisplayContext(
		String parameterValue) {

		ScopeSearchFacetDisplayBuilder scopeSearchFacetDisplayBuilder =
			new ScopeSearchFacetDisplayBuilder();

		scopeSearchFacetDisplayBuilder.setFacet(_facet);
		scopeSearchFacetDisplayBuilder.setFrequenciesVisible(true);
		scopeSearchFacetDisplayBuilder.setGroupLocalService(_groupLocalService);
		scopeSearchFacetDisplayBuilder.setParameterValue(parameterValue);

		return scopeSearchFacetDisplayBuilder.build();
	}

	protected Group createGroup(long groupId, String name) throws Exception {
		Group group = MockHelperUtil.setMethodAlwaysReturnExpected(
			Group.class, "getDescriptiveName", name, Locale.class);

		MockHelperUtil.setMethodAlwaysReturnExpected(
			group, "getGroupId", groupId);

		return group;
	}

	protected TermCollector createTermCollector(long groupId, int count)
		throws Exception {

		TermCollector termCollector =
			MockHelperUtil.setMethodAlwaysReturnExpected(
				TermCollector.class, "getFrequency", count);

		MockHelperUtil.setMethodAlwaysReturnExpected(
			termCollector, "getTerm", String.valueOf(groupId));

		return termCollector;
	}

	protected void setUpOneTermCollector(long groupId, int count)
		throws Exception {

		MockHelperUtil.setMethodAlwaysReturnExpected(
			_facetCollector, "getTermCollectors",
			Collections.singletonList(createTermCollector(groupId, count)));
	}

	private final Facet _facet = MockHelperUtil.initMock(Facet.class);
	private final FacetCollector _facetCollector = MockHelperUtil.initMock(
		FacetCollector.class);

	@Mock
	private GroupLocalService _groupLocalService;

}