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

package com.github.panpf.sketch.util

import com.github.panpf.sketch.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIScreen
import kotlin.math.roundToInt

/**
 * Return the application's total memory in bytes.
 *
 * @see com.github.panpf.sketch.core.ios.test.util.PlatformContextsIosTest.testMaxMemory
 */
actual fun PlatformContext.maxMemory(): Long {
    // TODO Get the accurate max memory on the ios platform
    return 512L * 1024L * 1024L // 512 MB
}

/**
 * Return the application's cache directory.
 *
 * @see com.github.panpf.sketch.core.ios.test.util.PlatformContextsIosTest.testAppCacheDirectory
 */
actual fun PlatformContext.appCacheDirectory(): Path? {
    val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true)
    return (paths.first() as String).toPath()
}

/**
 * Return the application's cache directory.
 *
 * @see com.github.panpf.sketch.core.ios.test.util.PlatformContextsIosTest.testScreenSize
 */
@OptIn(ExperimentalForeignApi::class)
actual fun PlatformContext.screenSize(): Size {
    val screen = UIScreen.mainScreen
    val scale = screen.scale
    val width = screen.bounds.useContents { size.width }
    val height = screen.bounds.useContents { size.height }
    val widthPixels = (width * scale).roundToInt()
    val heightPixels = (height * scale).roundToInt()
    return Size(width = widthPixels, height = heightPixels)
}