/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.cluster.multiple.internal.io;

import com.liferay.portal.kernel.io.SerializationConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Random;

/**
 * @author Lance Ji
 */
public class ClusterSerializerTest {
	@Before
	public void setUp() {
		System.clearProperty(
			ClusterSerializer.class.getName() + ".thread.local.buffer.count.limit");
		System.clearProperty(
			ClusterSerializer.class.getName() + ".thread.local.buffer.size.limit");
	}

	@Test
	public void testBufferOutputStream() {
		byte[] data = new byte[1024];

		_random.nextBytes(data);

		ClusterSerializer serializer = new ClusterSerializer();

		ClusterSerializer.BufferOutputStream bufferOutputStream =
			serializer.new BufferOutputStream();

		for (byte b : data) {
			bufferOutputStream.write(b);
		}

		byte[] result = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(data, result);

		serializer = new ClusterSerializer();

		bufferOutputStream = serializer.new BufferOutputStream();

		bufferOutputStream.write(data);

		result = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(data, result);
	}
	@Test
	public void testWriteByte() {
		ClusterSerializer serializer = new ClusterSerializer();

		byte[] bytes = new byte[_COUNT];

		_random.nextBytes(bytes);

		for (int i = 0; i < _COUNT; i++) {
			serializer.writeByte(bytes[i]);
		}

		Assert.assertArrayEquals(bytes, serializer.toByteBuffer().array());
	}

	@Test
	public void testWriteChar() {
		ClusterSerializer serializer = new ClusterSerializer();

		ByteBuffer byteBuffer = ByteBuffer.allocate(_COUNT * 2);

		byteBuffer.order(ByteOrder.BIG_ENDIAN);

		CharBuffer charBuffer = byteBuffer.asCharBuffer();

		char[] chars = new char[_COUNT];

		for (int i = 0; i < _COUNT; i++) {
			chars[i] = (char)_random.nextInt();

			serializer.writeChar(chars[i]);

			charBuffer.put(chars[i]);
		}

		byte[] bytes = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(byteBuffer.array(), bytes);
	}

	@Test
	public void testWriteDouble() {
		ClusterSerializer serializer = new ClusterSerializer();

		ByteBuffer byteBuffer = ByteBuffer.allocate(_COUNT * 8);

		byteBuffer.order(ByteOrder.BIG_ENDIAN);

		DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();

		double[] doubles = new double[_COUNT];

		for (int i = 0; i < _COUNT; i++) {
			doubles[i] = _random.nextDouble();

			serializer.writeDouble(doubles[i]);

			doubleBuffer.put(doubles[i]);
		}

		byte[] bytes = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(byteBuffer.array(), bytes);
	}

	@Test
	public void testWriteFloat() {
		ClusterSerializer serializer = new ClusterSerializer();

		ByteBuffer byteBuffer = ByteBuffer.allocate(_COUNT * 4);

		byteBuffer.order(ByteOrder.BIG_ENDIAN);

		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();

		float[] floats = new float[_COUNT];

		for (int i = 0; i < _COUNT; i++) {
			floats[i] = _random.nextFloat();

			serializer.writeFloat(floats[i]);

			floatBuffer.put(floats[i]);
		}

		byte[] bytes = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(byteBuffer.array(), bytes);
	}

	@Test
	public void testWriteInt() {
		ClusterSerializer serializer = new ClusterSerializer();

		ByteBuffer byteBuffer = ByteBuffer.allocate(_COUNT * 4);

		byteBuffer.order(ByteOrder.BIG_ENDIAN);

		IntBuffer intBuffer = byteBuffer.asIntBuffer();

		int[] ints = new int[_COUNT];

		for (int i = 0; i < _COUNT; i++) {
			ints[i] = _random.nextInt();

			serializer.writeInt(ints[i]);

			intBuffer.put(ints[i]);
		}

		byte[] bytes = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(byteBuffer.array(), bytes);
	}

	@Test
	public void testWriteObjectByte() {
		ClusterSerializer serializer = new ClusterSerializer();

		serializer.writeObject(Byte.valueOf((byte)101));

		ByteBuffer byteBuffer = serializer.toByteBuffer();

		Assert.assertEquals(2, byteBuffer.limit());
		Assert.assertEquals(SerializationConstants.TC_BYTE, byteBuffer.get());
		Assert.assertEquals(101, byteBuffer.get());
	}
	@Test
	public void testWriteLong() {
		ClusterSerializer serializer = new ClusterSerializer();

		ByteBuffer byteBuffer = ByteBuffer.allocate(_COUNT * 8);

		byteBuffer.order(ByteOrder.BIG_ENDIAN);

		LongBuffer longBuffer = byteBuffer.asLongBuffer();

		long[] longs = new long[_COUNT];

		for (int i = 0; i < _COUNT; i++) {
			longs[i] = _random.nextLong();

			serializer.writeLong(longs[i]);

			longBuffer.put(longs[i]);
		}

		byte[] bytes = serializer.toByteBuffer().array();

		Assert.assertArrayEquals(byteBuffer.array(), bytes);
	}

	private static final int _COUNT = 1024;

	private final Random _random = new Random();
}
