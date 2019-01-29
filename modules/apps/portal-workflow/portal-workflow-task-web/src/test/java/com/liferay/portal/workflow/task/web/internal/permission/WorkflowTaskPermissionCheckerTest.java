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

package com.liferay.portal.workflow.task.web.internal.permission;

import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceWrapper;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.kernel.workflow.BaseWorkflowHandler;
import com.liferay.portal.kernel.workflow.DefaultWorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.RegistryUtil;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Matchers;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Adam Brandizzi
 */
public class WorkflowTaskPermissionCheckerTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		setUpGroupLocalServiceUtil();
		setUpWorkflowHandlerRegistryUtil();
	}

	@Test
	public void testCompanyAdminHasPermission() {
		Assert.assertTrue(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(), mockWorkflowTask(),
				mockCompanyAdminPermissionChecker()));
	}

	@Test
	public void testContentReviewerHasPermission() {
		PermissionChecker permissionChecker =
			mockContentReviewerPermissionChecker(RandomTestUtil.randomLong());

		Assert.assertTrue(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(),
				mockWorkflowTask(
					User.class.getName(), permissionChecker.getUserId()),
				permissionChecker));
	}

	@Test
	public void testContentReviewerRoleHasPermission() {
		long[] permissionCheckerRoleIds = randomPermissionCheckerRoleIds();

		Assert.assertTrue(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(),
				mockWorkflowTask(
					Role.class.getName(), permissionCheckerRoleIds[0]),
				mockContentReviewerPermissionChecker(
					RandomTestUtil.randomLong(), permissionCheckerRoleIds)));
	}

	@Test
	public void testContentReviewerRoleWithAssetViewPermissionHasPermission()
		throws PortalException {

		mockAssetRendererHasViewPermission(true);

		long[] permissionCheckerRoleIds = randomPermissionCheckerRoleIds();

		Assert.assertTrue(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(),
				mockWorkflowTask(
					Role.class.getName(), permissionCheckerRoleIds[0]),
				mockPermissionChecker(
					RandomTestUtil.randomLong(), permissionCheckerRoleIds,
					false, false, false)));
	}

	@Test
	public void testNotAssigneeHasNoPermission() {
		long assigneeUserId = RandomTestUtil.randomLong();

		Assert.assertFalse(
			_workflowTaskPermissionChecker.hasPermission(
				assigneeUserId,
				mockWorkflowTask(User.class.getName(), assigneeUserId),
				mockContentReviewerPermissionChecker(
					RandomTestUtil.randomLong())));
	}

	@Test
	public void testNotAssigneeRoleHasNoPermission() {
		long assigneeRoleId = RandomTestUtil.randomLong();

		Assert.assertFalse(
			_workflowTaskPermissionChecker.hasPermission(
				assigneeRoleId,
				mockWorkflowTask(Role.class.getName(), assigneeRoleId),
				mockContentReviewerPermissionChecker(
					RandomTestUtil.randomLong())));
	}

	@Test
	public void testNotContentReviewerHasNoPermission() {
		Assert.assertFalse(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(), mockWorkflowTask(),
				mockPermissionChecker(
					RandomTestUtil.randomLong(), new long[0], false, false,
					false)));
	}

	@Test
	public void testNotContentReviewerWithAssetViewPermissionHasNoPermission()
		throws PortalException {

		mockAssetRendererHasViewPermission(true);

		Assert.assertFalse(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(), mockWorkflowTask(),
				mockPermissionChecker(
					RandomTestUtil.randomLong(), new long[0], false, false,
					false)));
	}

	@Test
	public void testNotContentReviewerWithAssetViewPermissionHasPermission()
		throws PortalException {

		// Checks permission on completed workflow task

		mockAssetRendererHasViewPermission(true);

		Assert.assertTrue(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(), mockCompletedWorkflowTask(),
				mockPermissionChecker(
					RandomTestUtil.randomLong(), new long[0], false, false,
					false)));
	}

	@Test
	public void testNotContentReviewerWithNoAssetViewPermissionHasNoPermission()
		throws PortalException {

		long[] permissionCheckerRoleIds = randomPermissionCheckerRoleIds();

		mockAssetRendererHasViewPermission(false);

		Assert.assertFalse(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(),
				mockWorkflowTask(
					Role.class.getName(), permissionCheckerRoleIds[0]),
				mockPermissionChecker(
					RandomTestUtil.randomLong(), permissionCheckerRoleIds,
					false, false, false)));
	}

	@Test
	public void testNotContentReviewerWithoutAssetViewPermissionHasNoPermission()
		throws PortalException {

		// Checks permission on completed workflow task

		mockAssetRendererHasViewPermission(false);

		Assert.assertFalse(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(), mockCompletedWorkflowTask(),
				mockPermissionChecker(
					RandomTestUtil.randomLong(), new long[0], false, false,
					false)));
	}

	@Test
	public void testOmniadminHasPermission() {
		Assert.assertTrue(
			_workflowTaskPermissionChecker.hasPermission(
				RandomTestUtil.randomLong(), mockWorkflowTask(),
				mockOmniadminPermissionChecker()));
	}

	protected void mockAssetRendererHasViewPermission(
			boolean hasAssetViewPermission)
		throws PortalException {

		AssetRenderer assetRenderer = mock(AssetRenderer.class);

		when(
			assetRenderer.hasViewPermission(
				Matchers.any(PermissionChecker.class))
		).thenReturn(
			hasAssetViewPermission
		);

		WorkflowHandler workflowHandler = new BaseWorkflowHandler() {

			@Override
			public AssetRenderer getAssetRenderer(long classPK)
				throws PortalException {

				return assetRenderer;
			}

			@Override
			public String getClassName() {
				return null;
			}

			@Override
			public String getType(Locale locale) {
				return null;
			}

			@Override
			public Object updateStatus(int status, Map workflowContext)
				throws PortalException {

				return null;
			}

		};

		Map<String, WorkflowHandler<?>> workflowHandlerMap =
			new TreeMap<String, WorkflowHandler<?>>() {

				@Override
				public WorkflowHandler<?> get(Object key) {
					return workflowHandler;
				}

			};

		ReflectionTestUtil.setFieldValue(
			_workflowHandlerRegistryUtil, "_workflowHandlerMap",
			workflowHandlerMap);
	}

	protected PermissionChecker mockCompanyAdminPermissionChecker() {
		return mockPermissionChecker(
			RandomTestUtil.randomLong(), new long[0], true, false, false);
	}

	protected WorkflowTask mockCompletedWorkflowTask() {
		return mockWorkflowTask(
			Role.class.getName(), RandomTestUtil.randomLong(), true);
	}

	protected PermissionChecker mockContentReviewerPermissionChecker(
		long userId) {

		return mockPermissionChecker(userId, new long[0], false, true, false);
	}

	protected PermissionChecker mockContentReviewerPermissionChecker(
		long userId, long[] roleIds) {

		return mockPermissionChecker(userId, roleIds, false, true, false);
	}

	protected PermissionChecker mockOmniadminPermissionChecker() {
		return mockPermissionChecker(
			RandomTestUtil.randomLong(), new long[0], false, false, true);
	}

	protected PermissionChecker mockPermissionChecker(
		long userId, long[] roleIds, boolean companyAdmin,
		boolean contentReviewer, boolean omniadmin) {

		PermissionChecker permissionChecker = mock(PermissionChecker.class);

		when(
			permissionChecker.getUserId()
		).thenReturn(
			userId
		);

		when(
			permissionChecker.getRoleIds(Matchers.anyLong(), Matchers.anyLong())
		).thenReturn(
			roleIds
		);

		when(
			permissionChecker.isCompanyAdmin()
		).thenReturn(
			companyAdmin
		);

		when(
			permissionChecker.isContentReviewer(
				Matchers.anyLong(), Matchers.anyLong())
		).thenReturn(
			contentReviewer
		);

		when(
			permissionChecker.isOmniadmin()
		).thenReturn(
			omniadmin
		);

		return permissionChecker;
	}

	protected WorkflowTask mockWorkflowTask() {
		return mockWorkflowTask(
			Role.class.getName(), RandomTestUtil.randomLong());
	}

	protected WorkflowTask mockWorkflowTask(
		String assigneeClassName, long assigneeClassPK) {

		return mockWorkflowTask(assigneeClassName, assigneeClassPK, false);
	}

	protected WorkflowTask mockWorkflowTask(
		String assigneeClassName, long assigneeClassPK, boolean completed) {

		WorkflowTaskAssignee workflowTaskAssignee = new WorkflowTaskAssignee(
			assigneeClassName, assigneeClassPK);

		List<WorkflowTaskAssignee> workflowTaskAssignees = new ArrayList<>();

		workflowTaskAssignees.add(workflowTaskAssignee);

		DefaultWorkflowTask workflowTask = new DefaultWorkflowTask() {

			@Override
			public Map<String, Serializable> getOptionalAttributes() {
				return new HashMap<>();
			}

			@Override
			public List<WorkflowTaskAssignee> getWorkflowTaskAssignees() {
				return workflowTaskAssignees;
			}

			@Override
			public boolean isCompleted() {
				return completed;
			}

		};

		return workflowTask;
	}

	protected long[] randomPermissionCheckerRoleIds() {
		return new long[] {RandomTestUtil.randomLong()};
	}

	protected void setUpGroupLocalServiceUtil() throws PortalException {
		ReflectionTestUtil.setFieldValue(
			GroupLocalServiceUtil.class, "_service",
			new GroupLocalServiceWrapper(null) {

				@Override
				public Group getGroup(long groupId) throws PortalException {
					return ProxyFactory.newDummyInstance(Group.class);
				}

			});
	}

	protected void setUpWorkflowHandlerRegistryUtil() throws Exception {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Constructor<WorkflowHandlerRegistryUtil> constructor =
			WorkflowHandlerRegistryUtil.class.getDeclaredConstructor();

		constructor.setAccessible(true);

		_workflowHandlerRegistryUtil = constructor.newInstance();

		ReflectionTestUtil.setFieldValue(
			WorkflowHandlerRegistryUtil.class, "_instance",
			_workflowHandlerRegistryUtil);
	}

	private WorkflowHandlerRegistryUtil _workflowHandlerRegistryUtil;
	private final WorkflowTaskPermissionChecker _workflowTaskPermissionChecker =
		new WorkflowTaskPermissionChecker();

}