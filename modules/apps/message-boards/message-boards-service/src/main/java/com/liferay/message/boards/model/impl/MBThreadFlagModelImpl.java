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

package com.liferay.message.boards.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.message.boards.model.MBThreadFlag;
import com.liferay.message.boards.model.MBThreadFlagModel;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model implementation for the MBThreadFlag service. Represents a row in the &quot;MBThreadFlag&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>MBThreadFlagModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link MBThreadFlagImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBThreadFlagImpl
 * @generated
 */
@ProviderType
public class MBThreadFlagModelImpl
	extends BaseModelImpl<MBThreadFlag> implements MBThreadFlagModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a message boards thread flag model instance should use the <code>MBThreadFlag</code> interface instead.
	 */
	public static final String TABLE_NAME = "MBThreadFlag";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"threadFlagId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"threadId", Types.BIGINT}, {"lastPublishDate", Types.TIMESTAMP}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("threadFlagId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("threadId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE =
		"create table MBThreadFlag (uuid_ VARCHAR(75) null,threadFlagId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,threadId LONG,lastPublishDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table MBThreadFlag";

	public static final String ORDER_BY_JPQL =
		" ORDER BY mbThreadFlag.threadFlagId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY MBThreadFlag.threadFlagId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.message.boards.service.util.ServiceProps.get(
			"value.object.entity.cache.enabled.com.liferay.message.boards.model.MBThreadFlag"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.message.boards.service.util.ServiceProps.get(
			"value.object.finder.cache.enabled.com.liferay.message.boards.model.MBThreadFlag"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.message.boards.service.util.ServiceProps.get(
			"value.object.column.bitmask.enabled.com.liferay.message.boards.model.MBThreadFlag"),
		true);

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long GROUPID_COLUMN_BITMASK = 2L;

	public static final long THREADID_COLUMN_BITMASK = 4L;

	public static final long USERID_COLUMN_BITMASK = 8L;

	public static final long UUID_COLUMN_BITMASK = 16L;

	public static final long THREADFLAGID_COLUMN_BITMASK = 32L;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.message.boards.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.message.boards.model.MBThreadFlag"));

	public MBThreadFlagModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _threadFlagId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setThreadFlagId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _threadFlagId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return MBThreadFlag.class;
	}

	@Override
	public String getModelClassName() {
		return MBThreadFlag.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<MBThreadFlag, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<MBThreadFlag, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<MBThreadFlag, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((MBThreadFlag)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<MBThreadFlag, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<MBThreadFlag, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(MBThreadFlag)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<MBThreadFlag, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<MBThreadFlag, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<MBThreadFlag, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<MBThreadFlag, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<MBThreadFlag, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<MBThreadFlag, Object>>();
		Map<String, BiConsumer<MBThreadFlag, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<MBThreadFlag, ?>>();

		attributeGetterFunctions.put("uuid", MBThreadFlag::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<MBThreadFlag, String>)MBThreadFlag::setUuid);
		attributeGetterFunctions.put(
			"threadFlagId", MBThreadFlag::getThreadFlagId);
		attributeSetterBiConsumers.put(
			"threadFlagId",
			(BiConsumer<MBThreadFlag, Long>)MBThreadFlag::setThreadFlagId);
		attributeGetterFunctions.put("groupId", MBThreadFlag::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<MBThreadFlag, Long>)MBThreadFlag::setGroupId);
		attributeGetterFunctions.put("companyId", MBThreadFlag::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<MBThreadFlag, Long>)MBThreadFlag::setCompanyId);
		attributeGetterFunctions.put("userId", MBThreadFlag::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<MBThreadFlag, Long>)MBThreadFlag::setUserId);
		attributeGetterFunctions.put("userName", MBThreadFlag::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<MBThreadFlag, String>)MBThreadFlag::setUserName);
		attributeGetterFunctions.put("createDate", MBThreadFlag::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<MBThreadFlag, Date>)MBThreadFlag::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", MBThreadFlag::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<MBThreadFlag, Date>)MBThreadFlag::setModifiedDate);
		attributeGetterFunctions.put("threadId", MBThreadFlag::getThreadId);
		attributeSetterBiConsumers.put(
			"threadId",
			(BiConsumer<MBThreadFlag, Long>)MBThreadFlag::setThreadId);
		attributeGetterFunctions.put(
			"lastPublishDate", MBThreadFlag::getLastPublishDate);
		attributeSetterBiConsumers.put(
			"lastPublishDate",
			(BiConsumer<MBThreadFlag, Date>)MBThreadFlag::setLastPublishDate);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@Override
	public long getThreadFlagId() {
		return _threadFlagId;
	}

	@Override
	public void setThreadFlagId(long threadFlagId) {
		_threadFlagId = threadFlagId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

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

	public long getOriginalUserId() {
		return _originalUserId;
	}

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

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

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

	@Override
	public long getThreadId() {
		return _threadId;
	}

	@Override
	public void setThreadId(long threadId) {
		_columnBitmask |= THREADID_COLUMN_BITMASK;

		if (!_setOriginalThreadId) {
			_setOriginalThreadId = true;

			_originalThreadId = _threadId;
		}

		_threadId = threadId;
	}

	public long getOriginalThreadId() {
		return _originalThreadId;
	}

	@Override
	public Date getLastPublishDate() {
		return _lastPublishDate;
	}

	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_lastPublishDate = lastPublishDate;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(MBThreadFlag.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), MBThreadFlag.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public MBThreadFlag toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = _escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		MBThreadFlagImpl mbThreadFlagImpl = new MBThreadFlagImpl();

		mbThreadFlagImpl.setUuid(getUuid());
		mbThreadFlagImpl.setThreadFlagId(getThreadFlagId());
		mbThreadFlagImpl.setGroupId(getGroupId());
		mbThreadFlagImpl.setCompanyId(getCompanyId());
		mbThreadFlagImpl.setUserId(getUserId());
		mbThreadFlagImpl.setUserName(getUserName());
		mbThreadFlagImpl.setCreateDate(getCreateDate());
		mbThreadFlagImpl.setModifiedDate(getModifiedDate());
		mbThreadFlagImpl.setThreadId(getThreadId());
		mbThreadFlagImpl.setLastPublishDate(getLastPublishDate());

		mbThreadFlagImpl.resetOriginalValues();

		return mbThreadFlagImpl;
	}

	@Override
	public int compareTo(MBThreadFlag mbThreadFlag) {
		long primaryKey = mbThreadFlag.getPrimaryKey();

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

		if (!(obj instanceof MBThreadFlag)) {
			return false;
		}

		MBThreadFlag mbThreadFlag = (MBThreadFlag)obj;

		long primaryKey = mbThreadFlag.getPrimaryKey();

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
		MBThreadFlagModelImpl mbThreadFlagModelImpl = this;

		mbThreadFlagModelImpl._originalUuid = mbThreadFlagModelImpl._uuid;

		mbThreadFlagModelImpl._originalGroupId = mbThreadFlagModelImpl._groupId;

		mbThreadFlagModelImpl._setOriginalGroupId = false;

		mbThreadFlagModelImpl._originalCompanyId =
			mbThreadFlagModelImpl._companyId;

		mbThreadFlagModelImpl._setOriginalCompanyId = false;

		mbThreadFlagModelImpl._originalUserId = mbThreadFlagModelImpl._userId;

		mbThreadFlagModelImpl._setOriginalUserId = false;

		mbThreadFlagModelImpl._setModifiedDate = false;

		mbThreadFlagModelImpl._originalThreadId =
			mbThreadFlagModelImpl._threadId;

		mbThreadFlagModelImpl._setOriginalThreadId = false;

		mbThreadFlagModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<MBThreadFlag> toCacheModel() {
		MBThreadFlagCacheModel mbThreadFlagCacheModel =
			new MBThreadFlagCacheModel();

		mbThreadFlagCacheModel.uuid = getUuid();

		String uuid = mbThreadFlagCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			mbThreadFlagCacheModel.uuid = null;
		}

		mbThreadFlagCacheModel.threadFlagId = getThreadFlagId();

		mbThreadFlagCacheModel.groupId = getGroupId();

		mbThreadFlagCacheModel.companyId = getCompanyId();

		mbThreadFlagCacheModel.userId = getUserId();

		mbThreadFlagCacheModel.userName = getUserName();

		String userName = mbThreadFlagCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			mbThreadFlagCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			mbThreadFlagCacheModel.createDate = createDate.getTime();
		}
		else {
			mbThreadFlagCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			mbThreadFlagCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			mbThreadFlagCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		mbThreadFlagCacheModel.threadId = getThreadId();

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			mbThreadFlagCacheModel.lastPublishDate = lastPublishDate.getTime();
		}
		else {
			mbThreadFlagCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		return mbThreadFlagCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<MBThreadFlag, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<MBThreadFlag, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<MBThreadFlag, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((MBThreadFlag)this));
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
		Map<String, Function<MBThreadFlag, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<MBThreadFlag, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<MBThreadFlag, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((MBThreadFlag)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final Function<InvocationHandler, MBThreadFlag>
		_escapedModelProxyProviderFunction = ProxyUtil.getProxyProviderFunction(
			MBThreadFlag.class, ModelWrapper.class);

	private String _uuid;
	private String _originalUuid;
	private long _threadFlagId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _threadId;
	private long _originalThreadId;
	private boolean _setOriginalThreadId;
	private Date _lastPublishDate;
	private long _columnBitmask;
	private MBThreadFlag _escapedModel;

}