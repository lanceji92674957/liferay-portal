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

package com.liferay.portal.cluster.multiple.internal;

import com.liferay.portal.kernel.io.AnnotatedObjectOutputStream;
import com.liferay.portal.kernel.io.SerializationConstants;
import com.liferay.portal.kernel.io.Serializer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Lance Ji
 */
public class ClusterSerializer extends Serializer {

	@Override
	public void writeObject(Serializable serializable) {
		if (serializable == null) {
			writeByte(SerializationConstants.TC_NULL);

			return;
		}
		else if (serializable instanceof Long) {
			writeByte(SerializationConstants.TC_LONG);
			writeLong((Long)serializable);

			return;
		}
		else if (serializable instanceof String) {
			writeByte(SerializationConstants.TC_STRING);
			writeString((String)serializable);

			return;
		}
		else if (serializable instanceof Integer) {
			writeByte(SerializationConstants.TC_INTEGER);
			writeInt((Integer)serializable);

			return;
		}
		else if (serializable instanceof Boolean) {
			writeByte(SerializationConstants.TC_BOOLEAN);
			writeBoolean((Boolean)serializable);

			return;
		}
		else if (serializable instanceof Class) {
			Class<?> clazz = (Class<?>)serializable;

			ClassLoader classLoader = clazz.getClassLoader();

			String contextName = ClusterClassLoaderPool.getContextName(
				classLoader);

			writeByte(SerializationConstants.TC_CLASS);
			writeString(contextName);
			writeString(clazz.getName());

			return;
		}
		else if (serializable instanceof Short) {
			writeByte(SerializationConstants.TC_SHORT);
			writeShort((Short)serializable);

			return;
		}
		else if (serializable instanceof Character) {
			writeByte(SerializationConstants.TC_CHARACTER);
			writeChar((Character)serializable);

			return;
		}
		else if (serializable instanceof Byte) {
			writeByte(SerializationConstants.TC_BYTE);
			writeByte((Byte)serializable);

			return;
		}
		else if (serializable instanceof Double) {
			writeByte(SerializationConstants.TC_DOUBLE);
			writeDouble((Double)serializable);

			return;
		}
		else if (serializable instanceof Float) {
			writeByte(SerializationConstants.TC_FLOAT);
			writeFloat((Float)serializable);

			return;
		}
		else {
			writeByte(SerializationConstants.TC_OBJECT);
		}

		try {
			ObjectOutputStream objectOutputStream =
				new AnnotatedObjectOutputStream(new BufferOutputStream());

			objectOutputStream.writeObject(serializable);

			objectOutputStream.flush();
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Unable to write ordinary serializable object " + serializable,
				ioe);
		}
	}

}