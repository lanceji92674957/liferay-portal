<%--
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
--%>

<%@ include file="/message_boards/init.jsp" %>

<%
MBMessageDisplay messageDisplay = (MBMessageDisplay)request.getAttribute(WebKeys.MESSAGE_BOARDS_MESSAGE_DISPLAY);

MBMessage message = messageDisplay.getMessage();

MBThread thread = messageDisplay.getThread();

AssetEntry layoutAssetEntry = AssetEntryLocalServiceUtil.getEntry(MBMessage.class.getName(), message.getMessageId());

request.setAttribute(WebKeys.LAYOUT_ASSET_ENTRY, layoutAssetEntry);

AssetEntryServiceUtil.incrementViewCounter(layoutAssetEntry);

boolean portletTitleBasedNavigation = GetterUtil.getBoolean(portletConfig.getInitParameter("portlet-title-based-navigation"));

MBBreadcrumbUtil.addPortletBreadcrumbEntries(message, request, renderResponse);
%>

<div <%= portletTitleBasedNavigation ? "class=\"container-fluid-1280\"" : StringPool.BLANK %>>
	<c:if test="<%= !portletTitleBasedNavigation %>">
		<liferay-util:include page="/message_boards/nav.jsp" servletContext="<%= application %>" />
	</c:if>

	<div <%= !portletTitleBasedNavigation ? "class=\"main-content-body\"" : StringPool.BLANK %>>
		<c:if test="<%= !portletTitleBasedNavigation %>">
			<liferay-ui:breadcrumb
				showCurrentGroup="<%= false %>"
				showGuestGroup="<%= false %>"
				showLayout="<%= false %>"
				showParentGroups="<%= false %>"
			/>
		</c:if>

		<liferay-util:include page="/message_boards/view_message_content.jsp" servletContext="<%= application %>" />
	</div>
</div>

<aui:script>
	function <portlet:namespace />addReplyToMessage(messageId, quote) {
		var addQuickReplyContainer = document.getElementById('<portlet:namespace />addReplyToMessage' + messageId);

		if (addQuickReplyContainer) {
			addQuickReplyContainer.classList.remove('hide');

			var parentMessageIdInput = addQuickReplyContainer.querySelector('#<portlet:namespace />parentMessageId');

			if (parentMessageIdInput) {
				parentMessageIdInput.value = messageId;
			}
		}

		var editorName = '<portlet:namespace />replyMessageBody' + messageId;

		if (!window[editorName].instanceReady) {
			window[editorName].create();
		}

		window[editorName].setHTML(quote);
		window[editorName].focus();

		if (addQuickReplyContainer && AUI().UA.mobile) {
			addQuickReplyContainer.scrollIntoView(true);
		}

		Liferay.Util.toggleDisabled('#<portlet:namespace />replyMessageButton' + messageId, true);
	}

	function <portlet:namespace />hideReplyMessage(messageId) {
		var addQuickReplyContainer = document.getElementById('<portlet:namespace />addReplyToMessage' + messageId);

		if (addQuickReplyContainer) {
			addQuickReplyContainer.classList.add('hide');
		}

		var editorName = '<portlet:namespace />replyMessageBody' + messageId;

		if (window[editorName]) {
			window[editorName].dispose();
		}
	}

	<c:if test="<%= thread.getRootMessageId() != message.getMessageId() %>">
		var message = document.getElementById('<portlet:namespace />message_' + <%= message.getMessageId() %>);

		if (message) {
			message.scrollIntoView(true);
		}
	</c:if>
</aui:script>

<%
MBThreadFlagLocalServiceUtil.addThreadFlag(themeDisplay.getUserId(), thread, new ServiceContext());

PortalUtil.setPageSubtitle(message.getSubject(), request);
PortalUtil.setPageDescription(message.getSubject(), request);

List<AssetTag> assetTags = AssetTagLocalServiceUtil.getTags(MBMessage.class.getName(), message.getMessageId());

PortalUtil.setPageKeywords(ListUtil.toString(assetTags, AssetTag.NAME_ACCESSOR), request);
%>