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

package com.liferay.portal.kernel.dao.orm;

import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.test.ProxyTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class IndexableActionableDynamicQueryTest {

	@Before
	public void setUp() {
		document1 = ProxyTestUtil.getDummyProxy(Document.class);

		document2 = ProxyTestUtil.getDummyProxy(Document.class);

		document3 = ProxyTestUtil.getDummyProxy(Document.class);

		Registry registry = new BasicRegistryImpl();

		registry.registerService(
			PortalExecutorManager.class,
			ProxyTestUtil.getDummyProxy(PortalExecutorManager.class));

		RegistryUtil.setRegistry(registry);

		indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexWriterHelper = ProxyTestUtil.getProxy(IndexWriterHelper.class);

		indexableActionableDynamicQuery.setIndexWriterHelper(indexWriterHelper);
	}

	@Test
	public void testAddDocuments() throws Exception {
		indexableActionableDynamicQuery.setInterval(1);

		indexableActionableDynamicQuery.addDocuments(document1, document2);

		verifyDocumentsUpdated(document1, document2);
	}

	@Test
	public void testAddDocumentsWithinInterval() throws Exception {
		indexableActionableDynamicQuery.setInterval(3);

		indexableActionableDynamicQuery.addDocuments(document1, document2);

		verifyNoDocumentsUpdated();

		indexableActionableDynamicQuery.addDocuments(document3);

		verifyDocumentsUpdated(document1, document2, document3);
	}

	protected void verifyDocumentsUpdated(Document... documents) {
		List<Object[]> argumentsList = ProxyTestUtil.getArgumentsList(
			indexWriterHelper, "updateDocuments");

		Assert.assertEquals(argumentsList.toString(), 1, argumentsList.size());

		Object[] arguments = argumentsList.get(0);

		Assert.assertNull(GetterUtil.getString(arguments[0]), arguments[0]);
		Assert.assertEquals(
			GetterUtil.getString(arguments[1]), 0L, arguments[1]);

		List<Document> documentList = (List<Document>)arguments[2];

		Assert.assertEquals(
			documentList.toString(), documents.length, documentList.size());

		for (int i = 0; i < documents.length; i++) {
			Assert.assertSame(documents[i], documentList.get(i));
		}

		Assert.assertFalse(
			GetterUtil.getString(arguments[3]), (boolean)arguments[3]);
	}

	protected void verifyNoDocumentsUpdated() {
		Map<String, List<Object[]>> proxyActions =
			ProxyTestUtil.getProxyActions(indexWriterHelper);

		Assert.assertTrue(proxyActions.toString(), proxyActions.isEmpty());
	}

	protected Document document1;
	protected Document document2;
	protected Document document3;
	protected IndexableActionableDynamicQuery indexableActionableDynamicQuery;
	protected IndexWriterHelper indexWriterHelper;

}