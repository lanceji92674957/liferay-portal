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
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskAssignmentInstanceLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskAssignmentInstanceLocalServiceWrapper;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

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
				new TestKaleoTaskAssignmentInstanceLocalServiceWrapper(null);

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(
				kaleoTaskInstanceToken,
				kaleoTaskAssignmentInstanceLocalService);

		lazyWorkflowTaskAssigneeList.initWorkflowTaskAssignees();

		int actualSize = lazyWorkflowTaskAssigneeList.size();

		Assert.assertFalse(((TestKaleoTaskAssignmentInstanceLocalServiceWrapper)
			kaleoTaskAssignmentInstanceLocalService).
				isGetKaleoTaskAssignmentInstancesCountExecuted());

		Assert.assertEquals(2, actualSize);
	}

	@Test
	public void testGetSizeWhenWorkflowTaskAssigneesIsNotLoaded() {
		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken();

		long kaleoTaskInstanceTokenId = RandomTestUtil.randomLong();

		Mockito.when(
			kaleoTaskInstanceToken.getKaleoTaskInstanceTokenId()
		).thenReturn(
			kaleoTaskInstanceTokenId
		);

		int expectedCount = RandomTestUtil.randomInt();

		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService =
				new TestKaleoTaskAssignmentInstanceLocalServiceWrapper(null) {

					@Override
					public int getKaleoTaskAssignmentInstancesCount(
						long taskInstanceTokenId) {

						setGetKaleoTaskAssignmentInstancesCountExecuted(true);

						if (kaleoTaskInstanceTokenId == taskInstanceTokenId) {
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

		Assert.assertTrue(((TestKaleoTaskAssignmentInstanceLocalServiceWrapper)
			kaleoTaskAssignmentInstanceLocalService).
				isGetKaleoTaskAssignmentInstancesCountExecuted());

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
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken(
				kaleoTaskAssignmentInstances);

		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService =
				ProxyFactory.newDummyInstance(
					KaleoTaskAssignmentInstanceLocalService.class);

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(
				kaleoTaskInstanceToken,
				kaleoTaskAssignmentInstanceLocalService);

		WorkflowTaskAssignee workflowTaskAssignee =
			lazyWorkflowTaskAssigneeList.get(1);

		verifyGetKaleoTaskAssignmentInstancesCall(
			kaleoTaskInstanceToken, Mockito.atLeastOnce());

		verifyGetFirstKaleoTaskAssignmentInstanceCall(
			kaleoTaskInstanceToken, Mockito.never());

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
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken(
				kaleoTaskAssignmentInstance);

		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService =
				ProxyFactory.newDummyInstance(
					KaleoTaskAssignmentInstanceLocalService.class);

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(
				kaleoTaskInstanceToken,
				kaleoTaskAssignmentInstanceLocalService);

		WorkflowTaskAssignee workflowTaskAssignee =
			lazyWorkflowTaskAssigneeList.get(0);

		verifyGetKaleoTaskAssignmentInstancesCall(
			kaleoTaskInstanceToken, Mockito.never());

		verifyGetFirstKaleoTaskAssignmentInstanceCall(
			kaleoTaskInstanceToken, Mockito.atLeastOnce());

		KaleoRuntimeTestUtil.assertWorkflowTaskAssignee(
			expectedAssigneeClassName, expectedAssigneeClassPK,
			workflowTaskAssignee);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetWhenIndexIsZeroAndAssignmentIsNull() {
		KaleoTaskInstanceToken kaleoTaskInstanceToken =
			KaleoRuntimeTestUtil.mockKaleoTaskInstanceToken();

		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService =
				ProxyFactory.newDummyInstance(
					KaleoTaskAssignmentInstanceLocalService.class);

		LazyWorkflowTaskAssigneeList lazyWorkflowTaskAssigneeList =
			new LazyWorkflowTaskAssigneeList(
				kaleoTaskInstanceToken,
				kaleoTaskAssignmentInstanceLocalService);

		lazyWorkflowTaskAssigneeList.get(0);
	}

	protected void verifyGetFirstKaleoTaskAssignmentInstanceCall(
		KaleoTaskInstanceToken kaleoTaskInstanceToken,
		VerificationMode verificationMode) {

		Mockito.verify(
			kaleoTaskInstanceToken, verificationMode
		).getFirstKaleoTaskAssignmentInstance();
	}

	protected void verifyGetKaleoTaskAssignmentInstancesCall(
		KaleoTaskInstanceToken kaleoTaskInstanceToken,
		VerificationMode verificationMode) {

		Mockito.verify(
			kaleoTaskInstanceToken, verificationMode
		).getKaleoTaskAssignmentInstances();
	}

	protected void verifyGetKaleoTaskAssignmentInstancesCountCall(
		KaleoTaskAssignmentInstanceLocalService
			kaleoTaskAssignmentInstanceLocalService,
		VerificationMode verificationMode) {

		Mockito.verify(
			kaleoTaskAssignmentInstanceLocalService, verificationMode
		).getKaleoTaskAssignmentInstancesCount(
			Matchers.anyLong()
		);
	}

	private class TestKaleoTaskAssignmentInstanceLocalServiceWrapper
		extends KaleoTaskAssignmentInstanceLocalServiceWrapper {

		@Override
		public int getKaleoTaskAssignmentInstancesCount(
			long kaleoTaskInstanceTokenId) {

			setGetKaleoTaskAssignmentInstancesCountExecuted(true);

			return super.getKaleoTaskAssignmentInstancesCount(
				kaleoTaskInstanceTokenId);
		}

		public boolean isGetKaleoTaskAssignmentInstancesCountExecuted() {
			return _getKaleoTaskAssignmentInstancesCountExecuted;
		}

		public void setGetKaleoTaskAssignmentInstancesCountExecuted(
			boolean getKaleoTaskAssignmentInstancesCountExecuted) {

			_getKaleoTaskAssignmentInstancesCountExecuted =
				getKaleoTaskAssignmentInstancesCountExecuted;
		}

		private TestKaleoTaskAssignmentInstanceLocalServiceWrapper(
			KaleoTaskAssignmentInstanceLocalService
				kaleoTaskAssignmentInstanceLocalService) {

			super(kaleoTaskAssignmentInstanceLocalService);
		}

		private boolean _getKaleoTaskAssignmentInstancesCountExecuted;

	}

}