package com.github.panpf.sketch.compose.core.common.test

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.github.panpf.sketch.SubcomposeAsyncImage
import com.github.panpf.sketch.images.ResourceImages
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.test.singleton.getTestContextAndSketch
import com.github.panpf.sketch.test.utils.LifecycleContainer
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SubcomposeAsyncImageTest {

    @Test
    fun testSubcomposeAsyncImage1() {
        val (_, sketch) = getTestContextAndSketch()
        runComposeUiTest {
            setContent {
                LifecycleContainer {
                    SubcomposeAsyncImage(ResourceImages.jpeg.uri, sketch, "test image")

                    SubcomposeAsyncImage(
                        ResourceImages.jpeg.uri,
                        sketch,
                        "test image",
                        Modifier,
                        rememberAsyncImageState(),
                        {},
                        {},
                        {},
                        Alignment.TopStart,
                        ContentScale.Crop,
                        0.5f,
                        ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        FilterQuality.High,
                        clipToBounds = false
                    )

                    SubcomposeAsyncImage(
                        uri = ResourceImages.jpeg.uri,
                        sketch = sketch,
                        contentDescription = "test image",
                        modifier = Modifier,
                        state = rememberAsyncImageState(),
                        loading = {},
                        success = {},
                        error = {},
                        alignment = Alignment.TopStart,
                        contentScale = ContentScale.Crop,
                        alpha = 0.5f,
                        colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        filterQuality = FilterQuality.High,
                        clipToBounds = false
                    )
                }
            }

            // TODO test: Screenshot test or draw to Bitmap, then compare Bitmap
        }
    }

    @Test
    fun testSubcomposeAsyncImage2() {
        val (_, sketch) = getTestContextAndSketch()
        runComposeUiTest {
            setContent {
                LifecycleContainer {
                    SubcomposeAsyncImage(
                        ResourceImages.jpeg.uri,
                        sketch,
                        "test image",
                        Modifier,
                        rememberAsyncImageState(),
                        Alignment.TopStart,
                        ContentScale.Crop,
                        0.5f,
                        ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        FilterQuality.High,
                    ) {

                    }

                    SubcomposeAsyncImage(
                        uri = ResourceImages.jpeg.uri,
                        sketch = sketch,
                        contentDescription = "test image",
                        modifier = Modifier,
                        state = rememberAsyncImageState(),
                        alignment = Alignment.TopStart,
                        contentScale = ContentScale.Crop,
                        alpha = 0.5f,
                        colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        filterQuality = FilterQuality.High,
                    ) {

                    }
                }
            }

            // TODO test: Screenshot test or draw to Bitmap, then compare Bitmap
        }
    }

    @Test
    fun testSubcomposeAsyncImage3() {
        val (_, sketch) = getTestContextAndSketch()
        runComposeUiTest {
            setContent {
                LifecycleContainer {
                    SubcomposeAsyncImage(
                        ComposableImageRequest(ResourceImages.jpeg.uri),
                        sketch,
                        "test image",
                    )

                    SubcomposeAsyncImage(
                        ComposableImageRequest(ResourceImages.jpeg.uri),
                        sketch,
                        "test image",
                        Modifier,
                        rememberAsyncImageState(),
                        {},
                        {},
                        {},
                        Alignment.TopStart,
                        ContentScale.Crop,
                        0.5f,
                        ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        FilterQuality.High,
                        clipToBounds = false
                    )

                    SubcomposeAsyncImage(
                        request = ComposableImageRequest(ResourceImages.jpeg.uri),
                        sketch = sketch,
                        contentDescription = "test image",
                        modifier = Modifier,
                        state = rememberAsyncImageState(),
                        loading = {},
                        success = {},
                        error = {},
                        alignment = Alignment.TopStart,
                        contentScale = ContentScale.Crop,
                        alpha = 0.5f,
                        colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        filterQuality = FilterQuality.High,
                        clipToBounds = false
                    )
                }
            }

            // TODO test: Screenshot test or draw to Bitmap, then compare Bitmap
        }
    }

    @Test
    fun testSubcomposeAsyncImage4() {
        val (_, sketch) = getTestContextAndSketch()
        runComposeUiTest {
            setContent {
                LifecycleContainer {
                    SubcomposeAsyncImage(
                        ComposableImageRequest(ResourceImages.jpeg.uri),
                        sketch,
                        "test image",
                        Modifier,
                        rememberAsyncImageState(),
                        Alignment.TopStart,
                        ContentScale.Crop,
                        0.5f,
                        ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        FilterQuality.High,
                    ) {

                    }

                    SubcomposeAsyncImage(
                        request = ComposableImageRequest(ResourceImages.jpeg.uri),
                        sketch = sketch,
                        contentDescription = "test image",
                        modifier = Modifier,
                        state = rememberAsyncImageState(),
                        alignment = Alignment.TopStart,
                        contentScale = ContentScale.Crop,
                        alpha = 0.5f,
                        colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.Red),
                        filterQuality = FilterQuality.High,
                    ) {

                    }
                }
            }

            // TODO test: Screenshot test or draw to Bitmap, then compare Bitmap
        }
    }
}