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

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.ResourceAction;
import com.liferay.portal.kernel.model.ResourceBlock;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class UpgradeKernelPackageTest extends UpgradeKernelPackage {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		connection = DataAccess.getConnection();
		_timestamp = new Timestamp(System.currentTimeMillis());

		_insertTableValues(
			"ClassName_", "0", String.valueOf(increment(ClassName.class)),
			StringBundler.concat("'PREFIX_", _OLD_CLASS_NAME, "'"));

		_insertTableValues(
			"Counter", StringBundler.concat("'", _OLD_CLASS_NAME, "'"), "10");

		_insertLockValues(_OLD_CLASS_NAME);

		_insertUserNotificationEventValues(_OLD_CLASS_NAME);

		_insertTableValues(
			"ResourceAction", "0",
			String.valueOf(increment(ResourceAction.class)),
			StringBundler.concat("'PREFIX_", _OLD_RESOURCE_NAME, "_POSTFIX'"),
			"'UPDATE'", "64");

		_insertTableValues(
			"ResourceBlock", "0",
			String.valueOf(increment(ResourceBlock.class)),
			String.valueOf(TestPropsValues.getCompanyId()),
			String.valueOf(TestPropsValues.getGroupId()),
			StringBundler.concat("'", _OLD_RESOURCE_NAME, "_POSTFIX'"),
			"'HASH'", "1");

		_insertTableValues(
			"ResourcePermission", "0",
			String.valueOf(increment(ResourcePermission.class)),
			String.valueOf(TestPropsValues.getCompanyId()),
			StringBundler.concat("'PREFIX_", _OLD_RESOURCE_NAME, "_POSTFIX'"),
			"4", "'PRIM_KEY'", "2", "3", "4", "5", "[$TRUE$]");
	}

	@After
	public void tearDown() throws Exception {
		for (String className : getClassNames()[0]) {
			runSQL(
				"delete from ClassName_ where value like '%" + className +
					"%'");

			runSQL("delete from Counter where name like '%" + className + "%'");

			runSQL(
				"delete from Lock_ where className like '%" + className + "%'");

			runSQL(
				"delete from UserNotificationEvent where payload like '%" +
					className + "%'");
		}

		for (String resourceName : getResourceNames()[0]) {
			runSQL(
				"delete from ResourceAction where name like '%" + resourceName +
					"%'");

			runSQL(
				"delete from ResourceBlock where name like '%" + resourceName +
					"%'");

			runSQL(
				"delete from ResourcePermission where name like '%" +
					resourceName + "%'");
		}

		connection.close();
	}

	@Test
	public void testSybaseLongTextUpdate() throws Exception {
		String updateSQL =
			"update UserNotificationEvent set payload = ? where " +
				"userNotificationEventId = ?";
		String selectSQL =
			"select payload, userNotificationEventId from " +
				"UserNotificationEvent where payload like '%" +
					_OLD_CLASS_NAME + "%'";

		String oldValue = _fetchValue(
			"UserNotificationEvent", "payload", _OLD_CLASS_NAME, true);
		upgradeLongTextTable(
			"payload", "userNotificationEventId", selectSQL, updateSQL,
			getClassNames()[0]);
		_checkUpgrade(
			oldValue, "UserNotificationEvent", "payload", _OLD_CLASS_NAME,
			_NEW_CLASS_NAME, true);
	}

	@Test
	public void testUpgradeClassName() throws Exception {
		assertUpgradeSuccessful(
			"ClassName_", "value", null, getClassNames()[0]);
	}

	@Test
	public void testUpgradeWithoutMatchData() throws Exception {
		Assert.assertNull(
			_fetchValue("ListType", "type_", _OLD_CLASS_NAME, false));
		upgradeTable(
			"ListType", "type_", getClassNames(), WildcardMode.TRAILING);
		Assert.assertNull(
			_fetchValue("ListType", "type_", _NEW_CLASS_NAME, false));
	}

	@Test
	public void testUserNotificationEvent() throws Exception {
		assertUpgradeSuccessful(
			"UserNotificationEvent", "payload", "userNotificationEventId",
			getClassNames()[0]);
	}

	protected void assertUpgradeSuccessful(
			String tableName, String columnName, String primaryKeyColumnName,
			String[] names)
		throws Exception {

		String oldValue = _fetchValue(tableName, columnName, names[0], true);

		if (primaryKeyColumnName != null) {
			upgradeLongTextTable(
				tableName, columnName, primaryKeyColumnName, getClassNames(),
				WildcardMode.SURROUND);
		}
		else {
			upgradeTable(
				tableName, columnName, getClassNames(), WildcardMode.SURROUND);
		}

		_checkUpgrade(
			oldValue, tableName, columnName, names[0], names[1], true);
	}

	@Override
	protected String[][] getClassNames() {
		return new String[][] {{_OLD_CLASS_NAME, _NEW_CLASS_NAME}};
	}

	@Override
	protected String[][] getResourceNames() {
		return new String[][] {{_OLD_RESOURCE_NAME, _NEW_RESOURCE_NAME}};
	}

	protected long increment(Class<?> clazz) throws Exception {
		return CounterLocalServiceUtil.increment(clazz.getName());
	}

	private void _checkUpgrade(
			String oldValue, String tableName, String columnName,
			String oldName, String newName, boolean expected)
		throws Exception {

		String newValue = StringUtil.replace(oldValue, oldName, newName);

		StringBundler newSelectSB = new StringBundler(9);

		newSelectSB.append("select ");
		newSelectSB.append(columnName);
		newSelectSB.append(" from ");
		newSelectSB.append(tableName);
		newSelectSB.append(" where ");
		newSelectSB.append(columnName);
		newSelectSB.append(" = '");
		newSelectSB.append(newValue);
		newSelectSB.append("'");

		try (PreparedStatement ps = connection.prepareStatement(
				newSelectSB.toString());
			ResultSet rs = ps.executeQuery()) {

			Assert.assertEquals(
				StringBundler.concat(
					"Table ", tableName, " and column ", columnName,
					" does not contain value ", newValue),
				expected, rs.next());
		}
	}

	private String _fetchValue(
			String tableName, String columnName, String value, boolean expected)
		throws Exception {

		StringBundler selectSB = new StringBundler(9);

		selectSB.append("select ");
		selectSB.append(columnName);
		selectSB.append(" from ");
		selectSB.append(tableName);
		selectSB.append(" where ");
		selectSB.append(columnName);
		selectSB.append(" like '%");
		selectSB.append(value);
		selectSB.append("%'");

		String oldValue = null;

		try (PreparedStatement ps = connection.prepareStatement(
				selectSB.toString());
			ResultSet rs = ps.executeQuery()) {

			String expectContain = " does contain value ";

			if (expected) {
				expectContain = " does not contain value ";
			}

			Assert.assertEquals(
				StringBundler.concat(
					"Table ", tableName, " and column ", columnName,
					expectContain, value),
				expected, rs.next());

			if (expected) {
				oldValue = rs.getString(columnName);
			}
		}

		return oldValue;
	}

	private void _insertLockValues(String value) throws Exception {
		StringBundler sb = new StringBundler(4);

		sb.append("insert into Lock_ (mvccVersion, uuid_, lockId, companyId, ");
		sb.append("userId, userName, createDate, className, key_, owner, ");
		sb.append("inheritable, expirationDate) values (?, ?, ?, ?, ?, ?, ?, ");
		sb.append("?, ?, ?, ?, ?)");

		PreparedStatement ps = connection.prepareStatement(sb.toString());

		ps.setLong(1, 0L);
		ps.setString(2, _UUID);
		ps.setLong(3, RandomTestUtil.nextLong());
		ps.setLong(4, TestPropsValues.getCompanyId());
		ps.setLong(5, TestPropsValues.getUserId());
		ps.setString(6, TestPropsValues.getUser().getFullName());
		ps.setTimestamp(7, _timestamp);
		ps.setString(8, StringBundler.concat("PREFIX_", value, "_POSTFIX"));
		ps.setString(9, "key_");
		ps.setString(10, "owner_");
		ps.setBoolean(11, true);
		ps.setTimestamp(12, _timestamp);

		ps.executeUpdate();
	}

	private void _insertTableValues(String tableName, String... values)
		throws Exception {

		StringBundler sb = new StringBundler(values.length);

		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" values(");

		for (String value : values) {
			sb.append(value);
			sb.append(StringPool.COMMA);
		}

		sb.setIndex(sb.index() - 1);
		sb.append(")");

		runSQL(sb.toString());
	}

	private void _insertUserNotificationEventValues(String value)
		throws Exception {

		StringBundler sb = new StringBundler(5);

		sb.append("insert into UserNotificationEvent (mvccVersion, uuid_, ");
		sb.append("userNotificationEventId, companyId, userId, type_, ");
		sb.append("timestamp, deliveryType, deliverBy, delivered, payload, ");
		sb.append("actionRequired, archived) values (?, ?, ?, ?, ?, ?, ?, ?, ");
		sb.append("?, ?, ?, ?, ?)");

		PreparedStatement ps = connection.prepareStatement(sb.toString());

		ps.setLong(1, 0L);
		ps.setString(2, _UUID);
		ps.setLong(3, RandomTestUtil.nextLong());
		ps.setLong(4, TestPropsValues.getCompanyId());
		ps.setLong(5, TestPropsValues.getUserId());
		ps.setString(6, "TYPE_");
		ps.setLong(7, RandomTestUtil.nextLong());
		ps.setInt(8, 0);
		ps.setLong(9, 0);
		ps.setShort(10, (short)0);
		ps.setString(11, StringBundler.concat("PREFIX_", value, "_POSTFIX"));
		ps.setShort(12, (short)0);
		ps.setShort(13, (short)0);

		ps.executeUpdate();
	}

	private static final String _NEW_CLASS_NAME =
		"com.liferay.class.path.kernel.Test";

	private static final String _NEW_RESOURCE_NAME =
		"com.liferay.resource.path.kernel.Test";

	private static final String _OLD_CLASS_NAME =
		"com.liferay.portlet.classpath.Test";

	private static final String _OLD_RESOURCE_NAME =
		"com.liferay.portlet.resourcepath.Test";

	private static final String _UUID = "theUuid";

	private Timestamp _timestamp;

}