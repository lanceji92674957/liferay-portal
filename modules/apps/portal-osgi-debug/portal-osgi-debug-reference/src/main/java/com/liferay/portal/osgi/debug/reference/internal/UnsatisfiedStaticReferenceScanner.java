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

package com.liferay.portal.osgi.debug.reference.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.osgi.debug.reference.internal.configuration.UnsatisfiedStaticReferenceScannerConfiguration;
import com.liferay.portal.osgi.reference.spi.UnresolvedStaticReferenceVisitorClient;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	configurationPid = "com.liferay.portal.osgi.debug.reference.internal.configuration.UnsatisfiedStaticReferenceScannerConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	service = {}
)
public class UnsatisfiedStaticReferenceScanner {

	@Activate
	protected void activate(Map<String, Object> properties) {
		UnsatisfiedStaticReferenceScannerConfiguration
			unsatisfiedStaticReferenceScannerConfiguration =
				ConfigurableUtil.createConfigurable(
					UnsatisfiedStaticReferenceScannerConfiguration.class,
					properties);

		long scanningInterval =
			unsatisfiedStaticReferenceScannerConfiguration.
				unsatisfiedStaticReferenceScanningInterval();

		if (scanningInterval > 0) {
			_unsatisfiedStaticReferenceScanningThread =
				new UnsatisfiedStaticReferenceScanningThread(
					scanningInterval * Time.SECOND,
					_unresolvedStaticReferenceVisitorClient);

			_unsatisfiedStaticReferenceScanningThread.start();
		}
	}

	@Deactivate
	protected void deactivate() throws InterruptedException {
		if (_unsatisfiedStaticReferenceScanningThread != null) {
			_unsatisfiedStaticReferenceScanningThread.interrupt();

			_unsatisfiedStaticReferenceScanningThread.join();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UnsatisfiedStaticReferenceScanner.class);

	@Reference
	private UnresolvedStaticReferenceVisitorClient
		_unresolvedStaticReferenceVisitorClient;

	private Thread _unsatisfiedStaticReferenceScanningThread;

	private static class UnsatisfiedStaticReferenceScanningThread
		extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					sleep(_scanningInterval);

					if (_log.isInfoEnabled()) {
						String message =
							StaticReferenceDependencyUtil.
								scanUnresolvedStaticReferences(
									_unresolvedStaticReferenceVisitorClient);

						if (message.isEmpty()) {
							_log.info("All static references are satisfied");
						}
						else if (_log.isWarnEnabled()) {
							_log.warn(message);
						}
					}
				}
			}
			catch (InterruptedException ie) {
				if (_log.isInfoEnabled()) {
					_log.info("Stopped scanning for static references");
				}
			}
		}

		private UnsatisfiedStaticReferenceScanningThread(
			long scanningInterval,
			UnresolvedStaticReferenceVisitorClient
				unresolvedStaticReferenceVisitorClient) {

			_scanningInterval = scanningInterval;
			_unresolvedStaticReferenceVisitorClient =
				unresolvedStaticReferenceVisitorClient;

			setDaemon(true);
			setName("Static References Scanner");
		}

		private final long _scanningInterval;
		private final UnresolvedStaticReferenceVisitorClient
			_unresolvedStaticReferenceVisitorClient;

	}

}