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

package com.liferay.portal.custom.sql.extender.internal;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.dao.orm.custom.sql.CustomSQLLoader;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.utils.extender.Extension;
import org.apache.felix.utils.log.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Lance Ji
 */
public class CustomSQLExtension implements Extension {

	public CustomSQLExtension(
		Bundle bundle, CustomSQL customSQL, Logger logger) {

		_bundle = bundle;
		_customSQL = customSQL;
		_logger = logger;

		_customSQLLoader = new CustomSQLLoaderImpl(_bundle);
	}

	@Override
	public void destroy() throws Exception {
		_serviceTracker.close();

		for (ServiceRegistration<CustomSQLLoader> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	public void start() throws Exception {
		final BundleContext bundleContext = _bundle.getBundleContext();

		String filterString = StringBundler.concat(
			"(objectClass=", CustomSQLLoader.class.getName(), ")");

		final Dictionary<String, Object> properties = new Hashtable<>();

		properties.put("bundle.symbolic.name", _bundle.getSymbolicName());

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, filterString,
			new ServiceTrackerCustomizer<CustomSQLLoader, CustomSQLLoader>() {

				@Override
				public CustomSQLLoader addingService(
					ServiceReference<CustomSQLLoader> serviceReference) {

					_serviceRegistrations.add(
						bundleContext.registerService(
							CustomSQLLoader.class, _customSQLLoader,
							properties));

					return bundleContext.getService(serviceReference);
				}

				@Override
				public void modifiedService(
					ServiceReference<CustomSQLLoader> serviceReference,
					CustomSQLLoader customSQLLoader) {

					removedService(serviceReference, customSQLLoader);

					addingService(serviceReference);
				}

				@Override
				public void removedService(
					ServiceReference<CustomSQLLoader> serviceReference,
					CustomSQLLoader customSQLLoader) {

					bundleContext.ungetService(serviceReference);
				}

			});
	}

	protected void registerCustomSQLLoader(
		Map<String, Object> attributes, CustomSQLLoader customSQLLoader) {
	}

	protected class CustomSQLLoaderImpl implements CustomSQLLoader {

		public CustomSQLLoaderImpl(Bundle bundle) {
			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

			_classLoader = bundleWiring.getClassLoader();
		}

		@Override
		public String appendCriteria(String sql, String criteria) {
			return _customSQL.appendCriteria(sql, criteria);
		}

		@Override
		public String get(String id) {
			if (_sqlMap == null) {
				_sqlMap = _loadCustomSQL(_classLoader);
			}

			return _sqlMap.get(id);
		}

		@Override
		public String get(String id, QueryDefinition<?> queryDefinition) {
			return get(id, queryDefinition, StringPool.BLANK);
		}

		@Override
		public String get(
			String id, QueryDefinition<?> queryDefinition, String tableName) {

			String sql = get(id);

			if (!Validator.isBlank(tableName) &&
				!tableName.endsWith(StringPool.PERIOD)) {

				tableName = tableName.concat(StringPool.PERIOD);
			}

			if (queryDefinition.getStatus() == WorkflowConstants.STATUS_ANY) {
				sql = sql.replace(_STATUS_KEYWORD, _STATUS_CONDITION_EMPTY);
			}
			else {
				if (queryDefinition.isExcludeStatus()) {
					sql = sql.replace(
						_STATUS_KEYWORD,
						tableName.concat(_STATUS_CONDITION_INVERSE));
				}
				else {
					sql = sql.replace(
						_STATUS_KEYWORD,
						tableName.concat(_STATUS_CONDITION_DEFAULT));
				}
			}

			if (queryDefinition.getOwnerUserId() > 0) {
				if (queryDefinition.isIncludeOwner()) {
					StringBundler sb = new StringBundler(7);

					sb.append(StringPool.OPEN_PARENTHESIS);
					sb.append(tableName);
					sb.append(_OWNER_USER_ID_CONDITION_DEFAULT);
					sb.append(" AND ");
					sb.append(tableName);
					sb.append(_STATUS_CONDITION_INVERSE);
					sb.append(StringPool.CLOSE_PARENTHESIS);

					sql = sql.replace(_OWNER_USER_ID_KEYWORD, sb.toString());

					sql = sql.replace(_OWNER_USER_ID_AND_OR_CONNECTOR, " OR ");
				}
				else {
					sql = sql.replace(
						_OWNER_USER_ID_KEYWORD,
						tableName.concat(_OWNER_USER_ID_CONDITION_DEFAULT));

					sql = sql.replace(_OWNER_USER_ID_AND_OR_CONNECTOR, " AND ");
				}
			}
			else {
				sql = sql.replace(_OWNER_USER_ID_KEYWORD, StringPool.BLANK);

				sql = sql.replace(
					_OWNER_USER_ID_AND_OR_CONNECTOR, StringPool.BLANK);
			}

			return sql;
		}

		@Override
		public String[] keywords(String keywords) {
			return _customSQL.keywords(keywords);
		}

		@Override
		public String[] keywords(String keywords, boolean lowerCase) {
			return _customSQL.keywords(keywords, lowerCase);
		}

		@Override
		public String[] keywords(
			String keywords, boolean lowerCase, WildcardMode wildcardMode) {

			return _customSQL.keywords(keywords, lowerCase, wildcardMode);
		}

		@Override
		public String[] keywords(String keywords, WildcardMode wildcardMode) {
			return _customSQL.keywords(keywords, wildcardMode);
		}

		@Override
		public String[] keywords(String[] keywordsArray) {
			return _customSQL.keywords(keywordsArray);
		}

		@Override
		public String[] keywords(String[] keywordsArray, boolean lowerCase) {
			return _customSQL.keywords(keywordsArray, lowerCase);
		}

		@Override
		public String removeGroupBy(String sql) {
			return _customSQL.removeGroupBy(sql);
		}

		@Override
		public String removeOrderBy(String sql) {
			return _customSQL.removeOrderBy(sql);
		}

		@Override
		public String replaceAndOperator(String sql, boolean andOperator) {
			return _customSQL.replaceAndOperator(sql, andOperator);
		}

		@Override
		public String replaceGroupBy(String sql, String groupBy) {
			return _customSQL.replaceGroupBy(sql, groupBy);
		}

		@Override
		public String replaceIsNull(String sql) {
			return _customSQL.replaceIsNull(sql);
		}

		@Override
		public String replaceKeywords(
			String sql, String field, boolean last, int[] values) {

			return _customSQL.replaceKeywords(sql, field, last, values);
		}

		@Override
		public String replaceKeywords(
			String sql, String field, boolean last, long[] values) {

			return _customSQL.replaceKeywords(sql, field, last, values);
		}

		@Override
		public String replaceKeywords(
			String sql, String field, String operator, boolean last,
			String[] values) {

			return _customSQL.replaceKeywords(
				sql, field, operator, last, values);
		}

		@Override
		public String replaceOrderBy(String sql, OrderByComparator<?> obc) {
			return _customSQL.replaceOrderBy(sql, obc);
		}

		protected String transform(String sql) {
			sql = PortalUtil.transformCustomSQL(sql);

			StringBundler sb = new StringBundler();

			try (UnsyncBufferedReader unsyncBufferedReader =
					new UnsyncBufferedReader(new UnsyncStringReader(sql))) {

				String line = null;

				while ((line = unsyncBufferedReader.readLine()) != null) {
					line = line.trim();

					if (line.startsWith(StringPool.CLOSE_PARENTHESIS)) {
						sb.setIndex(sb.index() - 1);
					}

					sb.append(line);

					if (!line.endsWith(StringPool.OPEN_PARENTHESIS)) {
						sb.append(StringPool.SPACE);
					}
				}
			}
			catch (IOException ioe) {
				return sql;
			}

			return sb.toString();
		}

		private Map<String, String> _loadCustomSQL(ClassLoader classLoader) {
			Map<String, String> sqls = new HashMap<>();

			try {
				_read(classLoader, "custom-sql/default.xml", sqls);
				_read(classLoader, "META-INF/custom-sql/default.xml", sqls);
			}
			catch (Exception e) {
				_logger.log(Logger.LOG_ERROR, e.getMessage());
			}

			return sqls;
		}

		private void _read(
				ClassLoader classLoader, String source,
				Map<String, String> sqls)
			throws Exception {

			try (InputStream is = classLoader.getResourceAsStream(source)) {
				if (is == null) {
					return;
				}

				Document document = UnsecureSAXReaderUtil.read(is);

				Element rootElement = document.getRootElement();

				for (Element sqlElement : rootElement.elements("sql")) {
					String file = sqlElement.attributeValue("file");

					if (Validator.isNotNull(file)) {
						_read(classLoader, file, sqls);
					}
					else {
						String id = sqlElement.attributeValue("id");
						String content = transform(sqlElement.getText());

						content = _customSQL.replaceIsNull(content);

						sqls.put(id, content);
					}
				}
			}
		}

		private static final String _OWNER_USER_ID_AND_OR_CONNECTOR =
			"[$OWNER_USER_ID_AND_OR_CONNECTOR$]";

		private static final String _OWNER_USER_ID_CONDITION_DEFAULT =
			"userId = ?";

		private static final String _OWNER_USER_ID_KEYWORD =
			"[$OWNER_USER_ID$]";

		private static final String _STATUS_CONDITION_DEFAULT = "status = ?";

		private static final String _STATUS_CONDITION_EMPTY =
			WorkflowConstants.STATUS_ANY + " = ?";

		private static final String _STATUS_CONDITION_INVERSE = "status != ?";

		private static final String _STATUS_KEYWORD = "[$STATUS$]";

		private final ClassLoader _classLoader;
		private Map<String, String> _sqlMap;

	}

	private final Bundle _bundle;
	private final CustomSQL _customSQL;
	private final CustomSQLLoaderImpl _customSQLLoader;
	private final Logger _logger;
	private final Collection<ServiceRegistration<CustomSQLLoader>>
		_serviceRegistrations = new ArrayList<>();
	private ServiceTracker<CustomSQLLoader, CustomSQLLoader> _serviceTracker;

}