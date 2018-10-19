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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.cluster;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,immediate = true, service = HealthClusterRequestExecutor.class)
public class HealthClusterRequestExecutorImpl
	implements HealthClusterRequestExecutor {

	@Override
	public HealthClusterResponse execute(
		HealthClusterRequest healthClusterRequest) {

		ClusterHealthRequestBuilder clusterHealthRequestBuilder =
			createClusterHealthRequestBuilder(healthClusterRequest);

		ClusterHealthResponse clusterHealthResponse =
			clusterHealthRequestBuilder.get();

		ClusterHealthStatus clusterHealthStatus =
			clusterHealthResponse.getStatus();

		return new HealthClusterResponse(
			clusterHealthStatusTranslator.translate(clusterHealthStatus),
			clusterHealthResponse.toString());
	}

	protected ClusterHealthRequestBuilder createClusterHealthRequestBuilder(
		HealthClusterRequest healthClusterRequest) {

		ClusterAdminClient clusterAdminClient =
			elasticsearchConnectionManager.getClusterAdminClient();

		return clusterAdminClient.prepareHealth(
			healthClusterRequest.getIndexNames());
	}

	@Reference
	protected ClusterHealthStatusTranslator clusterHealthStatusTranslator;

	@Reference
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

}