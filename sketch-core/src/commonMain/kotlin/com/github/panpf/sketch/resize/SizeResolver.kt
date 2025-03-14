/*
 * Copyright (C) 2024 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("RedundantConstructorKeyword")

package com.github.panpf.sketch.resize

import androidx.annotation.MainThread
import com.github.panpf.sketch.util.Key
import com.github.panpf.sketch.util.Size

/**
 * Create a [SizeResolver] that always returns the specified [Size]
 *
 * @see com.github.panpf.sketch.core.common.test.resize.SizeResolverTest.testCreateFunction
 */
fun SizeResolver(size: Size): SizeResolver = FixedSizeResolver(size)

/**
 * Create a [SizeResolver] that always returns the specified [Size]
 *
 * @see com.github.panpf.sketch.core.common.test.resize.SizeResolverTest.testCreateFunction
 */
fun SizeResolver(width: Int, height: Int): SizeResolver = FixedSizeResolver(width, height)

/**
 * An interface for measuring the target size for an image request.
 *
 * IMPORTANT: It is necessary to ensure compliance with the consistency principle,
 * that is, the equals() and hashCode() methods of instances created with the same
 * construction parameters return consistent results. This is important in Compose
 *
 * @see com.github.panpf.sketch.request.ImageRequest.Builder.size
 */
interface SizeResolver : Key {

    /** Return the [Size] that the image should be loaded at. */
    @MainThread
    suspend fun size(): Size

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String
}

/**
 * Returns the fixed size
 *
 * @see com.github.panpf.sketch.core.common.test.resize.SizeResolverTest
 */
data class FixedSizeResolver constructor(private val size: Size) : SizeResolver {

    constructor(width: Int, height: Int) : this(Size(width, height))

    override val key: String = "Fixed($size)"

    override suspend fun size(): Size = size

    override fun toString(): String = "FixedSizeResolver(size=$size)"
}