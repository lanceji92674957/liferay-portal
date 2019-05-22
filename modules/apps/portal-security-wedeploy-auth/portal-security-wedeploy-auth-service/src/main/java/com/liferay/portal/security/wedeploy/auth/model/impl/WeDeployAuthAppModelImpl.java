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

package com.liferay.portal.security.wedeploy.auth.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp;
import com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthAppModel;
import com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthAppSoap;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model implementation for the WeDeployAuthApp service. Represents a row in the &quot;WeDeployAuth_WeDeployAuthApp&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>WeDeployAuthAppModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link WeDeployAuthAppImpl}.
 * </p>
 *
 * @author Supritha Sundaram
 * @see WeDeployAuthAppImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class WeDeployAuthAppModelImpl
	extends BaseModelImpl<WeDeployAuthApp> implements WeDeployAuthAppModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a we deploy auth app model instance should use the <code>WeDeployAuthApp</code> interface instead.
	 */
	public static final String TABLE_NAME = "WeDeployAuth_WeDeployAuthApp";

	public static final Object[][] TABLE_COLUMNS = {
		{"weDeployAuthAppId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"name", Types.VARCHAR}, {"redirectURI", Types.VARCHAR},
		{"clientId", Types.VARCHAR}, {"clientSecret", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("weDeployAuthAppId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("redirectURI", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("clientId", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("clientSecret", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table WeDeployAuth_WeDeployAuthApp (weDeployAuthAppId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,name VARCHAR(75) null,redirectURI VARCHAR(75) null,clientId VARCHAR(75) null,clientSecret VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP =
		"drop table WeDeployAuth_WeDeployAuthApp";

	public static final String ORDER_BY_JPQL =
		" ORDER BY weDeployAuthApp.weDeployAuthAppId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY WeDeployAuth_WeDeployAuthApp.weDeployAuthAppId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.security.wedeploy.auth.service.util.ServiceProps.get(
			"value.object.entity.cache.enabled.com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.security.wedeploy.auth.service.util.ServiceProps.get(
			"value.object.finder.cache.enabled.com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.security.wedeploy.auth.service.util.ServiceProps.get(
			"value.object.column.bitmask.enabled.com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp"),
		true);

	public static final long CLIENTID_COLUMN_BITMASK = 1L;

	public static final long CLIENTSECRET_COLUMN_BITMASK = 2L;

	public static final long REDIRECTURI_COLUMN_BITMASK = 4L;

	public static final long WEDEPLOYAUTHAPPID_COLUMN_BITMASK = 8L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static WeDeployAuthApp toModel(WeDeployAuthAppSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		WeDeployAuthApp model = new WeDeployAuthAppImpl();

		model.setWeDeployAuthAppId(soapModel.getWeDeployAuthAppId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setName(soapModel.getName());
		model.setRedirectURI(soapModel.getRedirectURI());
		model.setClientId(soapModel.getClientId());
		model.setClientSecret(soapModel.getClientSecret());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<WeDeployAuthApp> toModels(
		WeDeployAuthAppSoap[] soapModels) {

		if (soapModels == null) {
			return null;
		}

		List<WeDeployAuthApp> models = new ArrayList<WeDeployAuthApp>(
			soapModels.length);

		for (WeDeployAuthAppSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.portal.security.wedeploy.auth.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp"));

	public WeDeployAuthAppModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _weDeployAuthAppId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setWeDeployAuthAppId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _weDeployAuthAppId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return WeDeployAuthApp.class;
	}

	@Override
	public String getModelClassName() {
		return WeDeployAuthApp.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WeDeployAuthApp, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<WeDeployAuthApp, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WeDeployAuthApp, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((WeDeployAuthApp)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WeDeployAuthApp, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<WeDeployAuthApp, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(WeDeployAuthApp)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<WeDeployAuthApp, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<WeDeployAuthApp, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<WeDeployAuthApp, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<WeDeployAuthApp, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<WeDeployAuthApp, Object>>
			attributeGetterFunctions =
				new LinkedHashMap<String, Function<WeDeployAuthApp, Object>>();
		Map<String, BiConsumer<WeDeployAuthApp, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<WeDeployAuthApp, ?>>();

		attributeGetterFunctions.put(
			"weDeployAuthAppId", WeDeployAuthApp::getWeDeployAuthAppId);
		attributeSetterBiConsumers.put(
			"weDeployAuthAppId",
			(BiConsumer<WeDeployAuthApp, Long>)
				WeDeployAuthApp::setWeDeployAuthAppId);
		attributeGetterFunctions.put(
			"companyId", WeDeployAuthApp::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<WeDeployAuthApp, Long>)WeDeployAuthApp::setCompanyId);
		attributeGetterFunctions.put("userId", WeDeployAuthApp::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<WeDeployAuthApp, Long>)WeDeployAuthApp::setUserId);
		attributeGetterFunctions.put("userName", WeDeployAuthApp::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<WeDeployAuthApp, String>)WeDeployAuthApp::setUserName);
		attributeGetterFunctions.put(
			"createDate", WeDeployAuthApp::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<WeDeployAuthApp, Date>)WeDeployAuthApp::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", WeDeployAuthApp::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<WeDeployAuthApp, Date>)
				WeDeployAuthApp::setModifiedDate);
		attributeGetterFunctions.put("name", WeDeployAuthApp::getName);
		attributeSetterBiConsumers.put(
			"name",
			(BiConsumer<WeDeployAuthApp, String>)WeDeployAuthApp::setName);
		attributeGetterFunctions.put(
			"redirectURI", WeDeployAuthApp::getRedirectURI);
		attributeSetterBiConsumers.put(
			"redirectURI",
			(BiConsumer<WeDeployAuthApp, String>)
				WeDeployAuthApp::setRedirectURI);
		attributeGetterFunctions.put("clientId", WeDeployAuthApp::getClientId);
		attributeSetterBiConsumers.put(
			"clientId",
			(BiConsumer<WeDeployAuthApp, String>)WeDeployAuthApp::setClientId);
		attributeGetterFunctions.put(
			"clientSecret", WeDeployAuthApp::getClientSecret);
		attributeSetterBiConsumers.put(
			"clientSecret",
			(BiConsumer<WeDeployAuthApp, String>)
				WeDeployAuthApp::setClientSecret);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getWeDeployAuthAppId() {
		return _weDeployAuthAppId;
	}

	@Override
	public void setWeDeployAuthAppId(long weDeployAuthAppId) {
		_weDeployAuthAppId = weDeployAuthAppId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return "";
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@JSON
	@Override
	public String getRedirectURI() {
		if (_redirectURI == null) {
			return "";
		}
		else {
			return _redirectURI;
		}
	}

	@Override
	public void setRedirectURI(String redirectURI) {
		_columnBitmask |= REDIRECTURI_COLUMN_BITMASK;

		if (_originalRedirectURI == null) {
			_originalRedirectURI = _redirectURI;
		}

		_redirectURI = redirectURI;
	}

	public String getOriginalRedirectURI() {
		return GetterUtil.getString(_originalRedirectURI);
	}

	@JSON
	@Override
	public String getClientId() {
		if (_clientId == null) {
			return "";
		}
		else {
			return _clientId;
		}
	}

	@Override
	public void setClientId(String clientId) {
		_columnBitmask |= CLIENTID_COLUMN_BITMASK;

		if (_originalClientId == null) {
			_originalClientId = _clientId;
		}

		_clientId = clientId;
	}

	public String getOriginalClientId() {
		return GetterUtil.getString(_originalClientId);
	}

	@JSON
	@Override
	public String getClientSecret() {
		if (_clientSecret == null) {
			return "";
		}
		else {
			return _clientSecret;
		}
	}

	@Override
	public void setClientSecret(String clientSecret) {
		_columnBitmask |= CLIENTSECRET_COLUMN_BITMASK;

		if (_originalClientSecret == null) {
			_originalClientSecret = _clientSecret;
		}

		_clientSecret = clientSecret;
	}

	public String getOriginalClientSecret() {
		return GetterUtil.getString(_originalClientSecret);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), WeDeployAuthApp.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public WeDeployAuthApp toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = _escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		WeDeployAuthAppImpl weDeployAuthAppImpl = new WeDeployAuthAppImpl();

		weDeployAuthAppImpl.setWeDeployAuthAppId(getWeDeployAuthAppId());
		weDeployAuthAppImpl.setCompanyId(getCompanyId());
		weDeployAuthAppImpl.setUserId(getUserId());
		weDeployAuthAppImpl.setUserName(getUserName());
		weDeployAuthAppImpl.setCreateDate(getCreateDate());
		weDeployAuthAppImpl.setModifiedDate(getModifiedDate());
		weDeployAuthAppImpl.setName(getName());
		weDeployAuthAppImpl.setRedirectURI(getRedirectURI());
		weDeployAuthAppImpl.setClientId(getClientId());
		weDeployAuthAppImpl.setClientSecret(getClientSecret());

		weDeployAuthAppImpl.resetOriginalValues();

		return weDeployAuthAppImpl;
	}

	@Override
	public int compareTo(WeDeployAuthApp weDeployAuthApp) {
		long primaryKey = weDeployAuthApp.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WeDeployAuthApp)) {
			return false;
		}

		WeDeployAuthApp weDeployAuthApp = (WeDeployAuthApp)obj;

		long primaryKey = weDeployAuthApp.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		WeDeployAuthAppModelImpl weDeployAuthAppModelImpl = this;

		weDeployAuthAppModelImpl._setModifiedDate = false;

		weDeployAuthAppModelImpl._originalRedirectURI =
			weDeployAuthAppModelImpl._redirectURI;

		weDeployAuthAppModelImpl._originalClientId =
			weDeployAuthAppModelImpl._clientId;

		weDeployAuthAppModelImpl._originalClientSecret =
			weDeployAuthAppModelImpl._clientSecret;

		weDeployAuthAppModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<WeDeployAuthApp> toCacheModel() {
		WeDeployAuthAppCacheModel weDeployAuthAppCacheModel =
			new WeDeployAuthAppCacheModel();

		weDeployAuthAppCacheModel.weDeployAuthAppId = getWeDeployAuthAppId();

		weDeployAuthAppCacheModel.companyId = getCompanyId();

		weDeployAuthAppCacheModel.userId = getUserId();

		weDeployAuthAppCacheModel.userName = getUserName();

		String userName = weDeployAuthAppCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			weDeployAuthAppCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			weDeployAuthAppCacheModel.createDate = createDate.getTime();
		}
		else {
			weDeployAuthAppCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			weDeployAuthAppCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			weDeployAuthAppCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		weDeployAuthAppCacheModel.name = getName();

		String name = weDeployAuthAppCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			weDeployAuthAppCacheModel.name = null;
		}

		weDeployAuthAppCacheModel.redirectURI = getRedirectURI();

		String redirectURI = weDeployAuthAppCacheModel.redirectURI;

		if ((redirectURI != null) && (redirectURI.length() == 0)) {
			weDeployAuthAppCacheModel.redirectURI = null;
		}

		weDeployAuthAppCacheModel.clientId = getClientId();

		String clientId = weDeployAuthAppCacheModel.clientId;

		if ((clientId != null) && (clientId.length() == 0)) {
			weDeployAuthAppCacheModel.clientId = null;
		}

		weDeployAuthAppCacheModel.clientSecret = getClientSecret();

		String clientSecret = weDeployAuthAppCacheModel.clientSecret;

		if ((clientSecret != null) && (clientSecret.length() == 0)) {
			weDeployAuthAppCacheModel.clientSecret = null;
		}

		return weDeployAuthAppCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<WeDeployAuthApp, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<WeDeployAuthApp, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WeDeployAuthApp, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((WeDeployAuthApp)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<WeDeployAuthApp, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<WeDeployAuthApp, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WeDeployAuthApp, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((WeDeployAuthApp)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final Function<InvocationHandler, WeDeployAuthApp>
		_escapedModelProxyProviderFunction = ProxyUtil.getProxyProviderFunction(
			WeDeployAuthApp.class, ModelWrapper.class);

	private long _weDeployAuthAppId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _name;
	private String _redirectURI;
	private String _originalRedirectURI;
	private String _clientId;
	private String _originalClientId;
	private String _clientSecret;
	private String _originalClientSecret;
	private long _columnBitmask;
	private WeDeployAuthApp _escapedModel;

}