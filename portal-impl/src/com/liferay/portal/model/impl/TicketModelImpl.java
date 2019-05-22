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

package com.liferay.portal.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.model.TicketModel;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;

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
 * The base model implementation for the Ticket service. Represents a row in the &quot;Ticket&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>TicketModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link TicketImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TicketImpl
 * @generated
 */
@ProviderType
public class TicketModelImpl
	extends BaseModelImpl<Ticket> implements TicketModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a ticket model instance should use the <code>Ticket</code> interface instead.
	 */
	public static final String TABLE_NAME = "Ticket";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ticketId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"createDate", Types.TIMESTAMP},
		{"classNameId", Types.BIGINT}, {"classPK", Types.BIGINT},
		{"key_", Types.VARCHAR}, {"type_", Types.INTEGER},
		{"extraInfo", Types.CLOB}, {"expirationDate", Types.TIMESTAMP}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ticketId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("key_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("type_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("extraInfo", Types.CLOB);
		TABLE_COLUMNS_MAP.put("expirationDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE =
		"create table Ticket (mvccVersion LONG default 0 not null,ticketId LONG not null primary key,companyId LONG,createDate DATE null,classNameId LONG,classPK LONG,key_ VARCHAR(75) null,type_ INTEGER,extraInfo TEXT null,expirationDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table Ticket";

	public static final String ORDER_BY_JPQL = " ORDER BY ticket.ticketId ASC";

	public static final String ORDER_BY_SQL = " ORDER BY Ticket.ticketId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.util.PropsUtil.get(
			"value.object.entity.cache.enabled.com.liferay.portal.kernel.model.Ticket"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.util.PropsUtil.get(
			"value.object.finder.cache.enabled.com.liferay.portal.kernel.model.Ticket"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.util.PropsUtil.get(
			"value.object.column.bitmask.enabled.com.liferay.portal.kernel.model.Ticket"),
		true);

	public static final long CLASSNAMEID_COLUMN_BITMASK = 1L;

	public static final long CLASSPK_COLUMN_BITMASK = 2L;

	public static final long COMPANYID_COLUMN_BITMASK = 4L;

	public static final long KEY_COLUMN_BITMASK = 8L;

	public static final long TYPE_COLUMN_BITMASK = 16L;

	public static final long TICKETID_COLUMN_BITMASK = 32L;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.portal.util.PropsUtil.get(
			"lock.expiration.time.com.liferay.portal.kernel.model.Ticket"));

	public TicketModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _ticketId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setTicketId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _ticketId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return Ticket.class;
	}

	@Override
	public String getModelClassName() {
		return Ticket.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Ticket, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<Ticket, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<Ticket, Object> attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((Ticket)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Ticket, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<Ticket, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(Ticket)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<Ticket, Object>> getAttributeGetterFunctions() {
		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<Ticket, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<Ticket, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<Ticket, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<Ticket, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<Ticket, Object>>();
		Map<String, BiConsumer<Ticket, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<Ticket, ?>>();

		attributeGetterFunctions.put("mvccVersion", Ticket::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion", (BiConsumer<Ticket, Long>)Ticket::setMvccVersion);
		attributeGetterFunctions.put("ticketId", Ticket::getTicketId);
		attributeSetterBiConsumers.put(
			"ticketId", (BiConsumer<Ticket, Long>)Ticket::setTicketId);
		attributeGetterFunctions.put("companyId", Ticket::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId", (BiConsumer<Ticket, Long>)Ticket::setCompanyId);
		attributeGetterFunctions.put("createDate", Ticket::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate", (BiConsumer<Ticket, Date>)Ticket::setCreateDate);
		attributeGetterFunctions.put("classNameId", Ticket::getClassNameId);
		attributeSetterBiConsumers.put(
			"classNameId", (BiConsumer<Ticket, Long>)Ticket::setClassNameId);
		attributeGetterFunctions.put("classPK", Ticket::getClassPK);
		attributeSetterBiConsumers.put(
			"classPK", (BiConsumer<Ticket, Long>)Ticket::setClassPK);
		attributeGetterFunctions.put("key", Ticket::getKey);
		attributeSetterBiConsumers.put(
			"key", (BiConsumer<Ticket, String>)Ticket::setKey);
		attributeGetterFunctions.put("type", Ticket::getType);
		attributeSetterBiConsumers.put(
			"type", (BiConsumer<Ticket, Integer>)Ticket::setType);
		attributeGetterFunctions.put("extraInfo", Ticket::getExtraInfo);
		attributeSetterBiConsumers.put(
			"extraInfo", (BiConsumer<Ticket, String>)Ticket::setExtraInfo);
		attributeGetterFunctions.put(
			"expirationDate", Ticket::getExpirationDate);
		attributeSetterBiConsumers.put(
			"expirationDate",
			(BiConsumer<Ticket, Date>)Ticket::setExpirationDate);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	@Override
	public long getTicketId() {
		return _ticketId;
	}

	@Override
	public void setTicketId(long ticketId) {
		_columnBitmask = -1L;

		_ticketId = ticketId;
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
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return "";
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
	public void setClassNameId(long classNameId) {
		_columnBitmask |= CLASSNAMEID_COLUMN_BITMASK;

		if (!_setOriginalClassNameId) {
			_setOriginalClassNameId = true;

			_originalClassNameId = _classNameId;
		}

		_classNameId = classNameId;
	}

	public long getOriginalClassNameId() {
		return _originalClassNameId;
	}

	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		_columnBitmask |= CLASSPK_COLUMN_BITMASK;

		if (!_setOriginalClassPK) {
			_setOriginalClassPK = true;

			_originalClassPK = _classPK;
		}

		_classPK = classPK;
	}

	public long getOriginalClassPK() {
		return _originalClassPK;
	}

	@Override
	public String getKey() {
		if (_key == null) {
			return "";
		}
		else {
			return _key;
		}
	}

	@Override
	public void setKey(String key) {
		_columnBitmask |= KEY_COLUMN_BITMASK;

		if (_originalKey == null) {
			_originalKey = _key;
		}

		_key = key;
	}

	public String getOriginalKey() {
		return GetterUtil.getString(_originalKey);
	}

	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		_columnBitmask |= TYPE_COLUMN_BITMASK;

		if (!_setOriginalType) {
			_setOriginalType = true;

			_originalType = _type;
		}

		_type = type;
	}

	public int getOriginalType() {
		return _originalType;
	}

	@Override
	public String getExtraInfo() {
		if (_extraInfo == null) {
			return "";
		}
		else {
			return _extraInfo;
		}
	}

	@Override
	public void setExtraInfo(String extraInfo) {
		_extraInfo = extraInfo;
	}

	@Override
	public Date getExpirationDate() {
		return _expirationDate;
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		_expirationDate = expirationDate;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), Ticket.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public Ticket toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = _escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		TicketImpl ticketImpl = new TicketImpl();

		ticketImpl.setMvccVersion(getMvccVersion());
		ticketImpl.setTicketId(getTicketId());
		ticketImpl.setCompanyId(getCompanyId());
		ticketImpl.setCreateDate(getCreateDate());
		ticketImpl.setClassNameId(getClassNameId());
		ticketImpl.setClassPK(getClassPK());
		ticketImpl.setKey(getKey());
		ticketImpl.setType(getType());
		ticketImpl.setExtraInfo(getExtraInfo());
		ticketImpl.setExpirationDate(getExpirationDate());

		ticketImpl.resetOriginalValues();

		return ticketImpl;
	}

	@Override
	public int compareTo(Ticket ticket) {
		int value = 0;

		if (getTicketId() < ticket.getTicketId()) {
			value = -1;
		}
		else if (getTicketId() > ticket.getTicketId()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Ticket)) {
			return false;
		}

		Ticket ticket = (Ticket)obj;

		long primaryKey = ticket.getPrimaryKey();

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
		TicketModelImpl ticketModelImpl = this;

		ticketModelImpl._originalCompanyId = ticketModelImpl._companyId;

		ticketModelImpl._setOriginalCompanyId = false;

		ticketModelImpl._originalClassNameId = ticketModelImpl._classNameId;

		ticketModelImpl._setOriginalClassNameId = false;

		ticketModelImpl._originalClassPK = ticketModelImpl._classPK;

		ticketModelImpl._setOriginalClassPK = false;

		ticketModelImpl._originalKey = ticketModelImpl._key;

		ticketModelImpl._originalType = ticketModelImpl._type;

		ticketModelImpl._setOriginalType = false;

		ticketModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<Ticket> toCacheModel() {
		TicketCacheModel ticketCacheModel = new TicketCacheModel();

		ticketCacheModel.mvccVersion = getMvccVersion();

		ticketCacheModel.ticketId = getTicketId();

		ticketCacheModel.companyId = getCompanyId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			ticketCacheModel.createDate = createDate.getTime();
		}
		else {
			ticketCacheModel.createDate = Long.MIN_VALUE;
		}

		ticketCacheModel.classNameId = getClassNameId();

		ticketCacheModel.classPK = getClassPK();

		ticketCacheModel.key = getKey();

		String key = ticketCacheModel.key;

		if ((key != null) && (key.length() == 0)) {
			ticketCacheModel.key = null;
		}

		ticketCacheModel.type = getType();

		ticketCacheModel.extraInfo = getExtraInfo();

		String extraInfo = ticketCacheModel.extraInfo;

		if ((extraInfo != null) && (extraInfo.length() == 0)) {
			ticketCacheModel.extraInfo = null;
		}

		Date expirationDate = getExpirationDate();

		if (expirationDate != null) {
			ticketCacheModel.expirationDate = expirationDate.getTime();
		}
		else {
			ticketCacheModel.expirationDate = Long.MIN_VALUE;
		}

		return ticketCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<Ticket, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<Ticket, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<Ticket, Object> attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((Ticket)this));
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
		Map<String, Function<Ticket, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<Ticket, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<Ticket, Object> attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((Ticket)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final Function<InvocationHandler, Ticket>
		_escapedModelProxyProviderFunction = ProxyUtil.getProxyProviderFunction(
			Ticket.class, ModelWrapper.class);

	private long _mvccVersion;
	private long _ticketId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private Date _createDate;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private String _key;
	private String _originalKey;
	private int _type;
	private int _originalType;
	private boolean _setOriginalType;
	private String _extraInfo;
	private Date _expirationDate;
	private long _columnBitmask;
	private Ticket _escapedModel;

}