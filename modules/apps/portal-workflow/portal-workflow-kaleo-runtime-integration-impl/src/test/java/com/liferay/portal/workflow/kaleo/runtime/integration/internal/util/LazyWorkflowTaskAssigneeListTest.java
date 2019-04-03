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

package com.liferay.portal.workflow.kaleo.runtime.integration.internal.util;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceTokenWrapper;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskAssignmentInstanceLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskAssignmentInstanceLocalServiceWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class LazyWorkflowTaskAssigneeListTest {

	@Test
	public void testGetSizeWhenWorkflowTaskAssigneesIsLoaded() {
		KaleoTaskAssignmentInstance[] kaleoTaskAssignmentInstances = {
			KaleoRuntimeTestUtil.mockKaleoTaskAssignmentInstance(
				Role.class.getName(), 1),
			KaleoRuntimeTestUtil.mockKaleoTaskAssignmentInstance(
				User.class.getName(), 2)
		};

		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken(
				kaleoTaskAssignmentInstances);

		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService =
				new KaleoTaskAssignmentInstanceLocalServiceWrapper(null) {

					@Override
					public int getKaleoTaskAssignmentInstancesCount(
						long kaleoTaskInstanceTokenId) {

						_executedMethodsSet.add(
							"getKaleoTaskAssignmentInstancesCount");

						return -1;
					}

				};

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(
				kaleoTaskInstanceToken,
				kaleoTaskAssignmentInstanceLocalService);

		lazyWorkflowTaskAssigneeList.initWorkflowTaskAssignees();

		int actualSize = lazyWorkflowTaskAssigneeList.size();

		Assert.assertFalse(
			"Method getKaleoTaskAssignmentInstancesCount should not be " +
				"invoked on kaleoTaskAssignmentInstanceLocalService",
			_executedMethodsSet.contains(
				"getKaleoTaskAssignmentInstancesCount"));

		Assert.assertEquals(2, actualSize);
	}

	@Test
	public void testGetSizeWhenWorkflowTaskAssigneesIsNotLoaded() {
		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken();

		long expectedKaleoTaskInstanceTokenId = RandomTestUtil.randomLong();

		kaleoTaskInstanceToken = new KaleoTaskInstanceTokenWrapper(
			kaleoTaskInstanceToken) {

			@Override
			public long getKaleoTaskInstanceTokenId() {
				return expectedKaleoTaskInstanceTokenId;
			}

		};

		int expectedCount = RandomTestUtil.randomInt();

		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService =
				new KaleoTaskAssignmentInstanceLocalServiceWrapper(null) {

					@Override
					public int getKaleoTaskAssignmentInstancesCount(
						long kaleoTaskInstanceTokenId) {

						_executedMethodsSet.add(
							"getKaleoTaskAssignmentInstancesCount");

						if (kaleoTaskInstanceTokenId ==
								expectedKaleoTaskInstanceTokenId) {

							return expectedCount;
						}

						return -1;
					}

				};

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(
				kaleoTaskInstanceToken,
				kaleoTaskAssignmentInstanceLocalService);

		int actualCount = lazyWorkflowTaskAssigneeList.size();

		Assert.assertTrue(
			"Method getKaleoTaskAssignmentInstancesCount should be invoked " +
				"on kaleoTaskAssignmentInstanceLocalService",
			_executedMethodsSet.contains(
				"getKaleoTaskAssignmentInstancesCount"));

		Assert.assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testGetWhenIndexIsGreaterThanZero() {
		KaleoTaskAssignmentInstance[] kaleoTaskAssignmentInstances = {
			KaleoRuntimeTestUtil.mockKaleoTaskAssignmentInstance(
				Role.class.getName(), 1),
			KaleoRuntimeTestUtil.mockKaleoTaskAssignmentInstance(
				User.class.getName(), 2)
		};

		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			new KaleoTaskInstanceTokenWrapper(
				KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken(
					kaleoTaskAssignmentInstances)) {

				@Override
				public KaleoTaskAssignmentInstance
					getFirstKaleoTaskAssignmentInstance() {

					_executedMethodsSet.add(
						"getFirstKaleoTaskAssignmentInstance");

					return super.getFirstKaleoTaskAssignmentInstance();
				}

				@Override
				public List<KaleoTaskAssignmentInstance>
					getKaleoTaskAssignmentInstances() {

					_executedMethodsSet.add("getKaleoTaskAssignmentInstances");

					return super.getKaleoTaskAssignmentInstances();
				}

			};

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(kaleoTaskInstanceToken, null);

		WorkflowTaskAssignee workflowTaskAssignee =
			lazyWorkflowTaskAssigneeList.get(1);

		Assert.assertTrue(
			"Method getKaleoTaskAssignmentInstances should be invoked on " +
				"kaleoTaskInstanceToken",
			_executedMethodsSet.contains("getKaleoTaskAssignmentInstances"));

		Assert.assertFalse(
			"Method getFirstKaleoTaskAssignmentInstance should not be " +
				"invoked on kaleoTaskInstanceToken",
			_executedMethodsSet.contains(
				"getFirstKaleoTaskAssignmentInstance"));

		KaleoRuntimeTestUtil.assertWorkflowTaskAssignee(
			User.class.getName(), 2, workflowTaskAssignee);
	}

	@Test
	public void testGetWhenIndexIsZeroAndAssignmentIsNotNull() {
		String expectedAssigneeClassName = StringUtil.randomString();

		long expectedAssigneeClassPK = RandomTestUtil.randomLong();

		KaleoTaskAssignmentInstance kaleoTaskAssignmentInstance =
			KaleoRuntimeTestUtil.mockKaleoTaskAssignmentInstance(
				expectedAssigneeClassName, expectedAssigneeClassPK);

		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			new KaleoTaskInstanceTokenWrapper(
				KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken(
					kaleoTaskAssignmentInstance)) {

				@Override
				public KaleoTaskAssignmentInstance
					getFirstKaleoTaskAssignmentInstance() {

					_executedMethodsSet.add(
						"getFirstKaleoTaskAssignmentInstance");

					return super.getFirstKaleoTaskAssignmentInstance();
				}

				@Override
				public List<KaleoTaskAssignmentInstance>
					getKaleoTaskAssignmentInstances() {

					_executedMethodsSet.add("getKaleoTaskAssignmentInstances");

					return super.getKaleoTaskAssignmentInstances();
				}

			};

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(kaleoTaskInstanceToken, null);

		WorkflowTaskAssignee workflowTaskAssignee =
			lazyWorkflowTaskAssigneeList.get(0);

		Assert.assertFalse(
			"Method getKaleoTaskAssignmentInstances should not be invoked on " +
				"kaleoTaskInstanceToken",
			_executedMethodsSet.contains("getKaleoTaskAssignmentInstances"));

		Assert.assertTrue(
			"Method getFirstKaleoTaskAssignmentInstance should be invoked on " +
				"kaleoTaskInstanceToken",
			_executedMethodsSet.contains(
				"getFirstKaleoTaskAssignmentInstance"));

		KaleoRuntimeTestUtil.assertWorkflowTaskAssignee(
			expectedAssigneeClassName, expectedAssigneeClassPK,
			workflowTaskAssignee);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetWhenIndexIsZeroAndAssignmentIsNull() {
		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken();

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(kaleoTaskInstanceToken, null);

		lazyWorkflowTaskAssigneeList.get(0);
	}

	private final Set<String> _executedMethodsSet = new HashSet<>();

}