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

import com.github.panpf.sketch.ComponentRegistry
import com.github.panpf.sketch.decode.internal.SkiaAnimatedDecoder
import com.github.panpf.sketch.decode.internal.isGif
import com.github.panpf.sketch.fetch.FetchResult
import com.github.panpf.sketch.request.RequestContext
import com.github.panpf.sketch.request.disallowAnimatedImage
import com.github.panpf.sketch.source.DataSource

/**
 * Adds gif support by Skia
 *
 * @see com.github.panpf.sketch.animated.gif.nonandroid.test.decode.GifSkiaAnimatedDecoderTest.testSupportGif
 */
fun ComponentRegistry.Builder.supportSkiaGif(): ComponentRegistry.Builder = apply {
    addDecoder(SkiaGifDecoder.Factory())
}

/**
 * Gif decoder based on Skia
 *
 * The following decoding related properties are supported:
 *
 * * colorType
 * * colorSpace
 * * disallowAnimatedImage
 * * repeatCount
 * * onAnimationStart
 * * onAnimationEnd
 * * cacheDecodeTimeoutFrame
 *
 * The following decoding related properties are not supported:
 *
 * * sizeResolver
 * * sizeMultiplier
 * * precisionDecider
 * * scaleDecider
 * * animatedTransformation
 *
 * @see com.github.panpf.sketch.animated.gif.nonandroid.test.decode.GifSkiaAnimatedDecoderTest
 */
class SkiaGifDecoder(
    requestContext: RequestContext,
    dataSource: DataSource,
) : SkiaAnimatedDecoder(requestContext, dataSource) {

    class Factory : Decoder.Factory {

        override val key: String = "SkiaGifDecoder"

        override fun create(requestContext: RequestContext, fetchResult: FetchResult): Decoder? {
            if (requestContext.request.disallowAnimatedImage == true) return null
            if (!isApplicable(fetchResult)) return null
            return SkiaGifDecoder(requestContext, fetchResult.dataSource)
        }

        private fun isApplicable(fetchResult: FetchResult): Boolean {
            return fetchResult.headerBytes.isGif()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            return other != null && this::class == other::class
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }

        override fun toString(): String = "SkiaGifDecoder"
    }
}