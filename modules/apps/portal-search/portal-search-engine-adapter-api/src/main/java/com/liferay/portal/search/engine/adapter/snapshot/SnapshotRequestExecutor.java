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

package com.liferay.portal.search.engine.adapter.snapshot;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface SnapshotRequestExecutor {

	public CreateSnapshotRepositoryResponse executeSnapshotRequest(
		CreateSnapshotRepositoryRequest createSnapshotRepositoryRequest);

	public CreateSnapshotResponse executeSnapshotRequest(
		CreateSnapshotRequest createSnapshotRequest);

	public DeleteSnapshotResponse executeSnapshotRequest(
		DeleteSnapshotRequest deleteSnapshotRequest);

	public GetSnapshotRepositoriesResponse executeSnapshotRequest(
		GetSnapshotRepositoriesRequest getSnapshotRepositoriesRequest);

	public GetSnapshotsResponse executeSnapshotRequest(
		GetSnapshotsRequest deleteSnapshotRequest);

	public RestoreSnapshotResponse executeSnapshotRequest(
		RestoreSnapshotRequest restoreSnapshotRequest);

}