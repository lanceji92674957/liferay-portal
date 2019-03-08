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

package com.liferay.portal.osgi.reference.spi.asm;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.osgi.reference.StaticReference;

import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author Preston Crary
 */
public class StaticReferenceASMParserUtil {

	public static void parseStaticReferences(
		byte[] classBytes, BiConsumer<String, String> biConsumer) {

		ClassReader classReader = new ClassReader(classBytes);

		FieldAnnotationClassVisitor fieldAnnotationClassVisitor =
			new FieldAnnotationClassVisitor();

		classReader.accept(fieldAnnotationClassVisitor, -1);

		for (FieldAnnotationFieldVisitor fieldAnnotationFieldVisitor :
				fieldAnnotationClassVisitor._fieldAnnotationFieldVisitors) {

			FieldAnnotationVisitor fieldAnnotationVisitor =
				fieldAnnotationFieldVisitor._fieldAnnotationVisitor;

			if (fieldAnnotationVisitor == null) {
				continue;
			}

			int access = fieldAnnotationFieldVisitor._access;

			if (!Modifier.isStatic(access) || Modifier.isFinal(access)) {
				String fieldName = fieldAnnotationFieldVisitor._fieldName;

				if (!Modifier.isStatic(access)) {
					_logInvalidField(classReader, fieldName, "non-static");
				}

				if (Modifier.isFinal(access)) {
					_logInvalidField(classReader, fieldName, "final");
				}

				continue;
			}

			String service = fieldAnnotationVisitor._service;

			if (service == null) {
				Type type = Type.getType(
					fieldAnnotationFieldVisitor._fieldDescriptor);

				service = type.getClassName();
			}

			String target = fieldAnnotationVisitor._target;

			if (target != null) {
				service = StringBundler.concat(
					"(&", target, "(objectClass=", service, "))");
			}

			biConsumer.accept(fieldAnnotationFieldVisitor._fieldName, service);
		}
	}

	private static void _logInvalidField(
		ClassReader classReader, String fieldName, String reason) {

		if (_log.isWarnEnabled()) {
			_log.warn(
				StringBundler.concat(
					"Invalid StaticReference in ", classReader.getClassName(),
					" on field ", fieldName, " because the field is ", reason));
		}
	}

	private StaticReferenceASMParserUtil() {
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StaticReferenceASMParserUtil.class);

	private static class FieldAnnotationClassVisitor extends ClassVisitor {

		@Override
		public FieldVisitor visitField(
			int access, String name, String descriptor, String signature,
			Object value) {

			FieldAnnotationFieldVisitor fieldAnnotationFieldVisitor =
				new FieldAnnotationFieldVisitor(access, name, descriptor);

			_fieldAnnotationFieldVisitors.add(fieldAnnotationFieldVisitor);

			return fieldAnnotationFieldVisitor;
		}

		private FieldAnnotationClassVisitor() {
			super(Opcodes.ASM5);
		}

		private final List<FieldAnnotationFieldVisitor>
			_fieldAnnotationFieldVisitors = new ArrayList<>();

	}

	private static class FieldAnnotationFieldVisitor extends FieldVisitor {

		@Override
		public AnnotationVisitor visitAnnotation(
			String descriptor, boolean visible) {

			if (_ANNOTATION_DESCRIPTOR.equals(descriptor)) {
				_fieldAnnotationVisitor = new FieldAnnotationVisitor();

				return _fieldAnnotationVisitor;
			}

			return super.visitAnnotation(descriptor, visible);
		}

		private FieldAnnotationFieldVisitor(
			int access, String fieldName, String fieldDescriptor) {

			super(Opcodes.ASM5);

			_access = access;
			_fieldName = fieldName;
			_fieldDescriptor = fieldDescriptor;
		}

		private static final String _ANNOTATION_DESCRIPTOR;

		static {
			Type type = Type.getType(StaticReference.class);

			_ANNOTATION_DESCRIPTOR = type.getDescriptor();
		}

		private final int _access;
		private FieldAnnotationVisitor _fieldAnnotationVisitor;
		private final String _fieldDescriptor;
		private final String _fieldName;

	}

	private static class FieldAnnotationVisitor extends AnnotationVisitor {

		@Override
		public void visit(String name, Object value) {
			if (name.equals("service")) {
				Type type = (Type)value;

				_service = type.getClassName();
			}
			else if (name.equals("target")) {
				_target = (String)value;
			}

			super.visit(name, value);
		}

		private FieldAnnotationVisitor() {
			super(Opcodes.ASM5);
		}

		private String _service;
		private String _target;

	}

}