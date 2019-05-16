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

package com.liferay.fragment.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentCollectionModel;
import com.liferay.fragment.model.FragmentCollectionSoap;
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
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

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
 * The base model implementation for the FragmentCollection service. Represents a row in the &quot;FragmentCollection&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>FragmentCollectionModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link FragmentCollectionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentCollectionImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class FragmentCollectionModelImpl
	extends BaseModelImpl<FragmentCollection>
	implements FragmentCollectionModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a fragment collection model instance should use the <code>FragmentCollection</code> interface instead.
	 */
	public static final String TABLE_NAME = "FragmentCollection";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"fragmentCollectionId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"fragmentCollectionKey", Types.VARCHAR}, {"name", Types.VARCHAR},
		{"description", Types.VARCHAR}, {"lastPublishDate", Types.TIMESTAMP}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("fragmentCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("fragmentCollectionKey", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE =
		"create table FragmentCollection (uuid_ VARCHAR(75) null,fragmentCollectionId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,fragmentCollectionKey VARCHAR(75) null,name VARCHAR(75) null,description STRING null,lastPublishDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table FragmentCollection";

	public static final String ORDER_BY_JPQL =
		" ORDER BY fragmentCollection.name ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY FragmentCollection.name ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long FRAGMENTCOLLECTIONKEY_COLUMN_BITMASK = 2L;

	public static final long GROUPID_COLUMN_BITMASK = 4L;

	public static final long NAME_COLUMN_BITMASK = 8L;

	public static final long UUID_COLUMN_BITMASK = 16L;

	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
		_entityCacheEnabled = entityCacheEnabled;
	}

	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
		_finderCacheEnabled = finderCacheEnabled;
	}

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static FragmentCollection toModel(FragmentCollectionSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		FragmentCollection model = new FragmentCollectionImpl();

		model.setUuid(soapModel.getUuid());
		model.setFragmentCollectionId(soapModel.getFragmentCollectionId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setFragmentCollectionKey(soapModel.getFragmentCollectionKey());
		model.setName(soapModel.getName());
		model.setDescription(soapModel.getDescription());
		model.setLastPublishDate(soapModel.getLastPublishDate());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<FragmentCollection> toModels(
		FragmentCollectionSoap[] soapModels) {

		if (soapModels == null) {
			return null;
		}

		List<FragmentCollection> models = new ArrayList<FragmentCollection>(
			soapModels.length);

		for (FragmentCollectionSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public FragmentCollectionModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _fragmentCollectionId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setFragmentCollectionId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _fragmentCollectionId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return FragmentCollection.class;
	}

	@Override
	public String getModelClassName() {
		return FragmentCollection.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<FragmentCollection, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<FragmentCollection, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<FragmentCollection, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((FragmentCollection)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<FragmentCollection, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<FragmentCollection, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(FragmentCollection)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<FragmentCollection, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<FragmentCollection, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<FragmentCollection, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<FragmentCollection, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<FragmentCollection, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<FragmentCollection, Object>>();
		Map<String, BiConsumer<FragmentCollection, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap<String, BiConsumer<FragmentCollection, ?>>();

		attributeGetterFunctions.put("uuid", FragmentCollection::getUuid);
		attributeSetterBiConsumers.put(
			"uuid",
			(BiConsumer<FragmentCollection, String>)
				FragmentCollection::setUuid);
		attributeGetterFunctions.put(
			"fragmentCollectionId",
			FragmentCollection::getFragmentCollectionId);
		attributeSetterBiConsumers.put(
			"fragmentCollectionId",
			(BiConsumer<FragmentCollection, Long>)
				FragmentCollection::setFragmentCollectionId);
		attributeGetterFunctions.put("groupId", FragmentCollection::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<FragmentCollection, Long>)
				FragmentCollection::setGroupId);
		attributeGetterFunctions.put(
			"companyId", FragmentCollection::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<FragmentCollection, Long>)
				FragmentCollection::setCompanyId);
		attributeGetterFunctions.put("userId", FragmentCollection::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<FragmentCollection, Long>)
				FragmentCollection::setUserId);
		attributeGetterFunctions.put(
			"userName", FragmentCollection::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<FragmentCollection, String>)
				FragmentCollection::setUserName);
		attributeGetterFunctions.put(
			"createDate", FragmentCollection::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<FragmentCollection, Date>)
				FragmentCollection::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", FragmentCollection::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<FragmentCollection, Date>)
				FragmentCollection::setModifiedDate);
		attributeGetterFunctions.put(
			"fragmentCollectionKey",
			FragmentCollection::getFragmentCollectionKey);
		attributeSetterBiConsumers.put(
			"fragmentCollectionKey",
			(BiConsumer<FragmentCollection, String>)
				FragmentCollection::setFragmentCollectionKey);
		attributeGetterFunctions.put("name", FragmentCollection::getName);
		attributeSetterBiConsumers.put(
			"name",
			(BiConsumer<FragmentCollection, String>)
				FragmentCollection::setName);
		attributeGetterFunctions.put(
			"description", FragmentCollection::getDescription);
		attributeSetterBiConsumers.put(
			"description",
			(BiConsumer<FragmentCollection, String>)
				FragmentCollection::setDescription);
		attributeGetterFunctions.put(
			"lastPublishDate", FragmentCollection::getLastPublishDate);
		attributeSetterBiConsumers.put(
			"lastPublishDate",
			(BiConsumer<FragmentCollection, Date>)
				FragmentCollection::setLastPublishDate);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
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

	@JSON
	@Override
	public long getFragmentCollectionId() {
		return _fragmentCollectionId;
	}

	@Override
	public void setFragmentCollectionId(long fragmentCollectionId) {
		_fragmentCollectionId = fragmentCollectionId;
	}

	@JSON
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

	@JSON
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
	public String getFragmentCollectionKey() {
		if (_fragmentCollectionKey == null) {
			return "";
		}
		else {
			return _fragmentCollectionKey;
		}
	}

	@Override
	public void setFragmentCollectionKey(String fragmentCollectionKey) {
		_columnBitmask |= FRAGMENTCOLLECTIONKEY_COLUMN_BITMASK;

		if (_originalFragmentCollectionKey == null) {
			_originalFragmentCollectionKey = _fragmentCollectionKey;
		}

		_fragmentCollectionKey = fragmentCollectionKey;
	}

	public String getOriginalFragmentCollectionKey() {
		return GetterUtil.getString(_originalFragmentCollectionKey);
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
		_columnBitmask = -1L;

		if (_originalName == null) {
			_originalName = _name;
		}

		_name = name;
	}

	public String getOriginalName() {
		return GetterUtil.getString(_originalName);
	}

	@JSON
	@Override
	public String getDescription() {
		if (_description == null) {
			return "";
		}
		else {
			return _description;
		}
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

	@JSON
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
			PortalUtil.getClassNameId(FragmentCollection.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), FragmentCollection.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public FragmentCollection toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (FragmentCollection)ProxyUtil.newProxyInstance(
				_classLoader, _escapedModelInterfaces,
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		FragmentCollectionImpl fragmentCollectionImpl =
			new FragmentCollectionImpl();

		fragmentCollectionImpl.setUuid(getUuid());
		fragmentCollectionImpl.setFragmentCollectionId(
			getFragmentCollectionId());
		fragmentCollectionImpl.setGroupId(getGroupId());
		fragmentCollectionImpl.setCompanyId(getCompanyId());
		fragmentCollectionImpl.setUserId(getUserId());
		fragmentCollectionImpl.setUserName(getUserName());
		fragmentCollectionImpl.setCreateDate(getCreateDate());
		fragmentCollectionImpl.setModifiedDate(getModifiedDate());
		fragmentCollectionImpl.setFragmentCollectionKey(
			getFragmentCollectionKey());
		fragmentCollectionImpl.setName(getName());
		fragmentCollectionImpl.setDescription(getDescription());
		fragmentCollectionImpl.setLastPublishDate(getLastPublishDate());

		fragmentCollectionImpl.resetOriginalValues();

		return fragmentCollectionImpl;
	}

	@Override
	public int compareTo(FragmentCollection fragmentCollection) {
		int value = 0;

		value = getName().compareTo(fragmentCollection.getName());

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

		if (!(obj instanceof FragmentCollection)) {
			return false;
		}

		FragmentCollection fragmentCollection = (FragmentCollection)obj;

		long primaryKey = fragmentCollection.getPrimaryKey();

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
		return _entityCacheEnabled;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _finderCacheEnabled;
	}

	@Override
	public void resetOriginalValues() {
		FragmentCollectionModelImpl fragmentCollectionModelImpl = this;

		fragmentCollectionModelImpl._originalUuid =
			fragmentCollectionModelImpl._uuid;

		fragmentCollectionModelImpl._originalGroupId =
			fragmentCollectionModelImpl._groupId;

		fragmentCollectionModelImpl._setOriginalGroupId = false;

		fragmentCollectionModelImpl._originalCompanyId =
			fragmentCollectionModelImpl._companyId;

		fragmentCollectionModelImpl._setOriginalCompanyId = false;

		fragmentCollectionModelImpl._setModifiedDate = false;

		fragmentCollectionModelImpl._originalFragmentCollectionKey =
			fragmentCollectionModelImpl._fragmentCollectionKey;

		fragmentCollectionModelImpl._originalName =
			fragmentCollectionModelImpl._name;

		fragmentCollectionModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<FragmentCollection> toCacheModel() {
		FragmentCollectionCacheModel fragmentCollectionCacheModel =
			new FragmentCollectionCacheModel();

		fragmentCollectionCacheModel.uuid = getUuid();

		String uuid = fragmentCollectionCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			fragmentCollectionCacheModel.uuid = null;
		}

		fragmentCollectionCacheModel.fragmentCollectionId =
			getFragmentCollectionId();

		fragmentCollectionCacheModel.groupId = getGroupId();

		fragmentCollectionCacheModel.companyId = getCompanyId();

		fragmentCollectionCacheModel.userId = getUserId();

		fragmentCollectionCacheModel.userName = getUserName();

		String userName = fragmentCollectionCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			fragmentCollectionCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			fragmentCollectionCacheModel.createDate = createDate.getTime();
		}
		else {
			fragmentCollectionCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			fragmentCollectionCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			fragmentCollectionCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		fragmentCollectionCacheModel.fragmentCollectionKey =
			getFragmentCollectionKey();

		String fragmentCollectionKey =
			fragmentCollectionCacheModel.fragmentCollectionKey;

		if ((fragmentCollectionKey != null) &&
			(fragmentCollectionKey.length() == 0)) {

			fragmentCollectionCacheModel.fragmentCollectionKey = null;
		}

		fragmentCollectionCacheModel.name = getName();

		String name = fragmentCollectionCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			fragmentCollectionCacheModel.name = null;
		}

		fragmentCollectionCacheModel.description = getDescription();

		String description = fragmentCollectionCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			fragmentCollectionCacheModel.description = null;
		}

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			fragmentCollectionCacheModel.lastPublishDate =
				lastPublishDate.getTime();
		}
		else {
			fragmentCollectionCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		return fragmentCollectionCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<FragmentCollection, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<FragmentCollection, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<FragmentCollection, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((FragmentCollection)this));
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
		Map<String, Function<FragmentCollection, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<FragmentCollection, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<FragmentCollection, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((FragmentCollection)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader =
		FragmentCollection.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
		FragmentCollection.class, ModelWrapper.class
	};
	private static boolean _entityCacheEnabled;
	private static boolean _finderCacheEnabled;

	private String _uuid;
	private String _originalUuid;
	private long _fragmentCollectionId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _fragmentCollectionKey;
	private String _originalFragmentCollectionKey;
	private String _name;
	private String _originalName;
	private String _description;
	private Date _lastPublishDate;
	private long _columnBitmask;
	private FragmentCollection _escapedModel;

}