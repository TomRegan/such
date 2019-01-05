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

package su.ch

import su.ch.annotation.Pure
import java.lang.Integer.toHexString

@Pure
object ToClassName : Function<String> {

    @JvmStatic fun toClassName(obj: Any) = invoke(obj)

    operator fun invoke(obj: Any): String =
            if (obj.javaClass.simpleName.isEmpty())
                obj.javaClass.name.substring(obj.javaClass.name.lastIndexOf(".") + 1) +
                        "@" + toHexString(obj.hashCode())
            else obj.javaClass.simpleName
}