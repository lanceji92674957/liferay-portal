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

package com.liferay.segments.web.internal.portlet.action;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.service.SegmentsEntryRelService;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo Garcia
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,
	immediate = true,
	property = {
		"javax.portlet.name=" + SegmentsPortletKeys.SEGMENTS,
		"mvc.command.name=deleteSegmentsEntryOrganization"
	},
	service = MVCActionCommand.class
)
public class DeleteSegmentsEntryOrganizationMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long segmentsEntryId = ParamUtil.getLong(
			actionRequest, "segmentsEntryId");

		long[] deleteSegmentsEntryOrganizationIds = null;

		long segmentsEntryOrganizationId = ParamUtil.getLong(
			actionRequest, "organizationId");

		if (segmentsEntryOrganizationId > 0) {
			deleteSegmentsEntryOrganizationIds =
				new long[] {segmentsEntryOrganizationId};
		}
		else {
			deleteSegmentsEntryOrganizationIds = ParamUtil.getLongValues(
				actionRequest, "rowIds");
		}

		for (long deleteSegmentsEntryOrganizationId :
				deleteSegmentsEntryOrganizationIds) {

			_segmentsEntryRelService.deleteSegmentsEntryRel(
				segmentsEntryId, _portal.getClassNameId(Organization.class),
				deleteSegmentsEntryOrganizationId);
		}
	}

	@Reference
	private Portal _portal;

	@Reference
	private SegmentsEntryRelService _segmentsEntryRelService;

}