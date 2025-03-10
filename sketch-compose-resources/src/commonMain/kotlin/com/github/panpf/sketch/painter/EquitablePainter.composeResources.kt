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

package com.github.panpf.sketch.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Create a [EquitablePainter] that wraps the specified [DrawableResource] and remembers it.
 *
 * @see com.github.panpf.sketch.compose.resources.common.test.painter.EquitablePainterComposeResourcesTest.testRememberEquitablePainterResource
 */
@Composable
fun rememberEquitablePainterResource(resource: DrawableResource): EquitablePainter {
    val painter = painterResource(resource)
    return remember(resource) {
        painter.asEquitable(equalityKey = resource)
    }
}


/**
 * Create a [EquitablePainter] that wraps the specified [DrawableResource].
 *
 * @see com.github.panpf.sketch.compose.resources.common.test.painter.EquitablePainterComposeResourcesTest.testEquitablePainterResource
 */
@Composable
fun equitablePainterResource(resource: DrawableResource): EquitablePainter {
    val painter = painterResource(resource)
    return painter.asEquitable(equalityKey = resource)
}