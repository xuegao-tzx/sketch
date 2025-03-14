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

package com.github.panpf.sketch.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.panpf.sketch.painter.rememberEquitablePainterResource
import org.jetbrains.compose.resources.DrawableResource

/**
 * Create a [PainterStateImage] that uses the specified [resource] as the image source and remembers it.
 *
 * @see com.github.panpf.sketch.compose.resources.common.test.state.PainterStateImageComposeResourcesTest.testRememberPainterStateImage
 */
@Composable
fun rememberPainterStateImage(resource: DrawableResource): PainterStateImage {
    val painter = rememberEquitablePainterResource(resource)
    return remember(resource) { PainterStateImage(painter) }
}