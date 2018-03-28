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

package com.liferay.adaptive.media.content.transformer;

/**
 * Transforms the content by invoking the {@link ContentTransformer} available
 * for a specific {@link ContentTransformerContentType}. There can be more than
 * one content transformer available for a particular content type, and they
 * will all be executed, but the order is not guaranteed.
 *
 * @author Alejandro Tardín
 */
public interface ContentTransformerHandler {

	public <T> T transform(
		ContentTransformerContentType<T> contentTransformerContentType,
		T originalContent);

}