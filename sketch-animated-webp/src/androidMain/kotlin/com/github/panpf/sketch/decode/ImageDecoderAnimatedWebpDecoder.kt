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

package com.github.panpf.sketch.decode

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.panpf.sketch.ComponentRegistry
import com.github.panpf.sketch.decode.internal.ImageDecoderAnimatedDecoder
import com.github.panpf.sketch.decode.internal.isAnimatedWebP
import com.github.panpf.sketch.fetch.FetchResult
import com.github.panpf.sketch.request.RequestContext
import com.github.panpf.sketch.request.disallowAnimatedImage
import com.github.panpf.sketch.source.DataSource

/**
 * Adds animation webp support by AnimatedImageDrawable
 *
 * @see com.github.panpf.sketch.animated.webp.android.test.decode.ImageDecoderAnimatedWebpDecoderTest.testSupportAnimatedWebp
 */
@RequiresApi(Build.VERSION_CODES.P)
fun ComponentRegistry.Builder.supportImageDecoderAnimatedWebp(): ComponentRegistry.Builder = apply {
    addDecoder(ImageDecoderAnimatedWebpDecoder.Factory())
}

/**
 * Decode webp animated image files using ImageDecoder
 *
 * The following decoding related properties are supported:
 *
 * * sizeResolver: Only sampleSize
 * * sizeMultiplier
 * * precisionDecider: Only LESS_PIXELS and SMALLER_SIZE is supported
 * * colorSpace
 * * disallowAnimatedImage
 * * repeatCount
 * * animatedTransformation
 * * onAnimationStart
 * * onAnimationEnd
 *
 * The following decoding related properties are not supported:
 *
 * * scaleDecider
 * * colorType
 *
 * @see com.github.panpf.sketch.animated.webp.android.test.decode.ImageDecoderAnimatedWebpDecoderTest
 */
@RequiresApi(Build.VERSION_CODES.P)
class ImageDecoderAnimatedWebpDecoder(
    requestContext: RequestContext,
    dataSource: DataSource,
) : ImageDecoderAnimatedDecoder(requestContext, dataSource) {

    class Factory : Decoder.Factory {

        override val key: String = "ImageDecoderAnimatedWebpDecoder"

        override fun create(
            requestContext: RequestContext,
            fetchResult: FetchResult
        ): Decoder? {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return null
            if (requestContext.request.disallowAnimatedImage == true) return null
            if (!isApplicable(fetchResult)) return null
            return ImageDecoderAnimatedWebpDecoder(requestContext, fetchResult.dataSource)
        }

        private fun isApplicable(fetchResult: FetchResult): Boolean {
            return fetchResult.headerBytes.isAnimatedWebP()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            return other != null && this::class == other::class
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }

        override fun toString(): String = "ImageDecoderAnimatedWebpDecoder"
    }
}