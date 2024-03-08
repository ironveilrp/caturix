/*
 * Caturix, a command processing library
 * Copyright (C) Intake team and contributors
 * Copyright (C) Caturix team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ironveilrp.caturix.parametric.annotation

/**
 * Marks other annotations as a "classifier."
 *
 *
 * Classifiers are special annotations that are used to differentiate
 * bindings for the same base type. A binding that has a classifier
 * defined will only provide values for parameters that have that
 * classifier, and the binding will also have precedence over
 * another binding that only handles the base type.
 *
 *
 * If an annotation is not annotated with this annotation, then
 * it will be considered a "modifier" and will be available to
 * providers; however, it will not be considered in choosing
 * the most appropriate binding for a parameter.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Classifier
