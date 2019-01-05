/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package su.ch.annotation

/**
 * Impure functions are allowed to yield actions and may or may not have a return value.
 *
 * Impure functions are allowed to have different effects each time they are called.
 * Object mutators, and methods which perform IO are examples of Impure programming.
 */
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
@Experimental
annotation class Impure
