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

package com.liferay.knowledge.base.internal.model.listener;

import com.liferay.knowledge.base.model.KBTemplate;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.staging.model.listener.StagingModelListener;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Akos Thurzo
 */
@Component(configurationPolicy = org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE,immediate = true, service = ModelListener.class)
public class KBTemplateStagingModelListener
	extends BaseModelListener<KBTemplate> {

	@Override
	public void onAfterCreate(KBTemplate kbTemplate)
		throws ModelListenerException {

		_stagingModelListener.onAfterCreate(kbTemplate);
	}

	@Override
	public void onAfterRemove(KBTemplate kbTemplate)
		throws ModelListenerException {

		_stagingModelListener.onAfterRemove(kbTemplate);
	}

	@Override
	public void onAfterUpdate(KBTemplate kbTemplate)
		throws ModelListenerException {

		_stagingModelListener.onAfterUpdate(kbTemplate);
	}

	@Reference
	private StagingModelListener<KBTemplate> _stagingModelListener;

}