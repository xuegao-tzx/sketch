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

package com.github.panpf.sketch.fetch.internal

import androidx.annotation.Keep
import com.github.panpf.sketch.PlatformContext
import com.github.panpf.sketch.fetch.KtorHttpUriFetcher
import com.github.panpf.sketch.util.ComponentLoader
import com.github.panpf.sketch.util.FetcherProvider

/**
 * Cooperate with [ComponentLoader] to achieve automatic registration [KtorHttpUriFetcher]
 *
 * @see com.github.panpf.sketch.http.ktor3.jvm.test.fetch.internal.KtorHttpUriFetcherProviderJvmTest
 */
@Keep
actual class KtorHttpUriFetcherProvider : FetcherProvider {

    actual override fun factory(context: PlatformContext): KtorHttpUriFetcher.Factory {
        return KtorHttpUriFetcher.Factory()
    }
}