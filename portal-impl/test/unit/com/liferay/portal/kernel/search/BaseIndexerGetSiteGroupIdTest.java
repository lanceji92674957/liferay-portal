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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupWrapper;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Miguel Angelo Caldas Gallindo
 * @author André de Oliveira
 */
public class BaseIndexerGetSiteGroupIdTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		ReflectionTestUtil.setFieldValue(
			GroupLocalServiceUtil.class, "_service", _groupLocalService);

		PropsUtil.setProps(ProxyFactory.newDummyInstance(Props.class));

		Registry registry = new BasicRegistryImpl();

		RegistryUtil.setRegistry(registry);

		_indexer = new TestIndexer();
	}

	@Test
	public void testGetSiteGroupId() throws Exception {
		long groupId = RandomTestUtil.randomLong();

		setUpGroup(groupId, false);

		Assert.assertEquals(groupId, _indexer.getSiteGroupId(groupId));
	}

	@Test
	public void testGetSiteGroupIdLayout() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		long parentGroupId = RandomTestUtil.randomLong();

		setUpLayoutGroup(groupId, parentGroupId, false);

		Assert.assertEquals(parentGroupId, _indexer.getSiteGroupId(groupId));
	}

	@Test
	public void testGetSiteGroupIdNonexistent() throws Exception {
		long groupId = RandomTestUtil.randomLong();

		setUpNonexistentGroup(groupId);

		Assert.assertEquals(groupId, _indexer.getSiteGroupId(groupId));
	}

	@Test
	public void testIsStagingGroup() throws Exception {
		long groupId = RandomTestUtil.randomLong();

		setUpGroup(groupId, true);

		Assert.assertEquals(true, _indexer.isStagingGroup(groupId));
	}

	@Test
	public void testIsStagingGroupLayout() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		long parentGroupId = RandomTestUtil.randomLong();

		setUpLayoutGroup(groupId, parentGroupId, true);

		Assert.assertEquals(true, _indexer.isStagingGroup(groupId));
	}

	@Test
	public void testIsStagingGroupNonexistent() throws Exception {
		long groupId = RandomTestUtil.randomLong();

		setUpNonexistentGroup(groupId);

		Assert.assertEquals(false, _indexer.isStagingGroup(groupId));
	}

	protected Group setUpGroup(long groupId, boolean stagingGroup)
		throws Exception {

		Group group = new GroupWrapper(null) {

			@Override
			public long getGroupId() {
				return groupId;
			}

			@Override
			public boolean isLayout() {
				return false;
			}

			@Override
			public boolean isStagingGroup() {
				return stagingGroup;
			}

		};

		Mockito.when(
			_groupLocalService.getGroup(groupId)
		).thenReturn(
			group
		);

		return group;
	}

	protected Group setUpLayoutGroup(
			long groupId, long parentGroupId, boolean stagingGroup)
		throws PortalException {

		Group parentGroup = new GroupWrapper(null) {

			@Override
			public long getGroupId() {
				return parentGroupId;
			}

			@Override
			public boolean isStagingGroup() {
				return stagingGroup;
			}

		};

		Group group = new GroupWrapper(null) {

			@Override
			public long getGroupId() {
				return groupId;
			}

			@Override
			public Group getParentGroup() {
				return parentGroup;
			}

			@Override
			public long getParentGroupId() {
				return parentGroupId;
			}

			@Override
			public boolean isLayout() {
				return true;
			}

		};

		Mockito.when(
			_groupLocalService.getGroup(groupId)
		).thenReturn(
			group
		);

		Mockito.when(
			_groupLocalService.getGroup(parentGroupId)
		).thenReturn(
			parentGroup
		);

		return parentGroup;
	}

	protected void setUpNonexistentGroup(long groupId) throws PortalException {
		Mockito.doThrow(
			new NoSuchGroupException()
		).when(
			_groupLocalService
		).getGroup(
			groupId
		);
	}

	@Mock
	private GroupLocalService _groupLocalService;

	private BaseIndexer<Object> _indexer;

	private static class TestIndexer extends BaseIndexer<Object> {

		@Override
		public String getClassName() {
			return null;
		}

		@Override
		protected void doDelete(Object object) throws Exception {
		}

		@Override
		protected Document doGetDocument(Object object) throws Exception {
			return null;
		}

		@Override
		protected Summary doGetSummary(
				Document document, Locale locale, String snippet,
				PortletRequest portletRequest, PortletResponse portletResponse)
			throws Exception {

			return null;
		}

		@Override
		protected void doReindex(Object object) throws Exception {
		}

		@Override
		protected void doReindex(String className, long classPK)
			throws Exception {
		}

		@Override
		protected void doReindex(String[] ids) throws Exception {
		}

	}

}