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

package com.github.panpf.sketch.core.common.test.transform

import com.github.panpf.sketch.BitmapImage
import com.github.panpf.sketch.images.ResourceImages
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.size
import com.github.panpf.sketch.test.singleton.getTestContextAndSketch
import com.github.panpf.sketch.test.utils.TestColor
import com.github.panpf.sketch.test.utils.corners
import com.github.panpf.sketch.test.utils.decode
import com.github.panpf.sketch.test.utils.toRequestContext
import com.github.panpf.sketch.transform.BlurTransformation
import com.github.panpf.sketch.transform.createBlurTransformed
import com.github.panpf.sketch.transform.getBlurTransformed
import com.github.panpf.sketch.util.Size
import com.github.panpf.sketch.util.hasAlphaPixels
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BlurTransformationTest {

    @Test
    fun testConstructor() {
        assertFailsWith(IllegalArgumentException::class) {
            BlurTransformation(0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            BlurTransformation(101)
        }
        assertFailsWith(IllegalArgumentException::class) {
            BlurTransformation(
                hasAlphaBitmapBgColor = TestColor.withA(color = TestColor.BLACK, a = 244)
            )
        }
        BlurTransformation(12).apply {
            assertEquals(12, radius)
            assertEquals(TestColor.BLACK, hasAlphaBitmapBgColor)
            assertNull(maskColor)
        }
        BlurTransformation(20, hasAlphaBitmapBgColor = null, maskColor = TestColor.GREEN).apply {
            assertEquals(20, radius)
            assertNull(hasAlphaBitmapBgColor)
            assertEquals(TestColor.GREEN, maskColor)
        }
    }

    @Test
    fun testKey() {
        BlurTransformation().apply {
            assertEquals("BlurTransformation(15,${TestColor.BLACK},null)", key)
        }
        BlurTransformation(20, hasAlphaBitmapBgColor = null, maskColor = TestColor.GREEN).apply {
            assertEquals("BlurTransformation(20,null,${TestColor.GREEN})", key)
        }
    }

    @Test
    fun testToString() {
        BlurTransformation().apply {
            assertEquals(
                "BlurTransformation(radius=15, hasAlphaBitmapBgColor=${TestColor.BLACK}, maskColor=null)",
                toString()
            )
        }
        BlurTransformation(20, hasAlphaBitmapBgColor = null, maskColor = TestColor.GREEN).apply {
            assertEquals(
                "BlurTransformation(radius=20, hasAlphaBitmapBgColor=null, maskColor=${TestColor.GREEN})",
                toString()
            )
        }
    }

    @Test
    fun testTransform() = runTest {
        val (context, sketch) = getTestContextAndSketch()
        val jpegRequest = ImageRequest(context, ResourceImages.jpeg.uri) {
            size(Size.Origin)
        }
        val jpegRequestContext = jpegRequest.toRequestContext(sketch)

        val inBitmap = ResourceImages.jpeg.decode().apply {
            assertFalse(bitmap.hasAlphaPixels())
        }
        inBitmap.apply {
            assertNotEquals(listOf(0, 0, 0, 0), this.corners())
            assertEquals(Size(1291, 1936), this.size)
        }
        val inBitmapCorners = inBitmap.corners()

        val transformResult = BlurTransformation(
            radius = 30,
            maskColor = TestColor.withA(TestColor.BLUE, 80)
        ).transform(jpegRequestContext, inBitmap)!!
        transformResult.apply {
            assertNotSame(inBitmap, image)
            assertNotEquals(inBitmapCorners, image.corners())
            assertEquals(Size(1291, 1936), image.size)
            assertEquals(
                expected = createBlurTransformed(
                    radius = 30,
                    hasAlphaBitmapBgColor = TestColor.BLACK,
                    maskColor = TestColor.withA(TestColor.BLUE, 80)
                ),
                actual = transformed
            )
        }

        // hasAlphaBitmapBgColor
        val pngRequest = ImageRequest(context, ResourceImages.png.uri) {
            size(Size.Origin)
        }
        val pngRequestContext = pngRequest.toRequestContext(sketch)
        val hasAlphaBitmap1 = pngRequest.decode(sketch).image.apply {
            assertTrue((this as BitmapImage).bitmap.hasAlphaPixels())
        }
        val hasAlphaBitmapBlurred1 = BlurTransformation(30).transform(
            requestContext = pngRequestContext,
            input = hasAlphaBitmap1
        )!!.image.apply {
            assertFalse((this as BitmapImage).bitmap.hasAlphaPixels())
        }

        val hasAlphaBitmap2 = pngRequest.decode(sketch).image.apply {
            assertTrue((this as BitmapImage).bitmap.hasAlphaPixels())
        }
        val hasAlphaBitmapBlurred2 = BlurTransformation(30, hasAlphaBitmapBgColor = null)
            .transform(pngRequestContext, hasAlphaBitmap2)!!.image.apply {
                assertTrue((this as BitmapImage).bitmap.hasAlphaPixels())
            }
        assertNotEquals(hasAlphaBitmapBlurred1.corners(), hasAlphaBitmapBlurred2.corners())
    }

    @Test
    fun testEqualsAndHashCode() {
        val element1 = BlurTransformation(20, null, null)
        val element11 = BlurTransformation(20, null, null)
        val element2 = BlurTransformation(10, TestColor.GREEN, null)
        val element3 = BlurTransformation(20, TestColor.BLACK, TestColor.BLUE)
        val element4 = BlurTransformation(20, TestColor.BLACK, TestColor.WHITE)

        assertEquals(element1, element11)
        assertNotEquals(element1, element2)
        assertNotEquals(element1, element3)
        assertNotEquals(element1, element4)
        assertNotEquals(element2, element3)
        assertNotEquals(element2, element4)
        assertNotEquals(element3, element4)
        assertNotEquals(element1, null as Any?)
        assertNotEquals(element1, Any())

        assertEquals(element1.hashCode(), element11.hashCode())
        assertNotEquals(element1.hashCode(), element2.hashCode())
        assertNotEquals(element2.hashCode(), element3.hashCode())
    }

    @Test
    fun testBlurTransformed() {
        assertEquals("BlurTransformed(1,null,null)", createBlurTransformed(1, null, null))
        assertEquals("BlurTransformed(2,null,null)", createBlurTransformed(2, null, null))
        assertEquals("BlurTransformed(4,null,null)", createBlurTransformed(4, null, null))
        assertEquals("BlurTransformed(8,null,null)", createBlurTransformed(8, null, null))

        assertEquals(null, listOf<String>().getBlurTransformed())
        assertEquals(
            "BlurTransformed(2,null,null)",
            listOf(createBlurTransformed(2, null, null)).getBlurTransformed()
        )
        assertEquals(
            "BlurTransformed(16,null,null)",
            listOf(
                "disruptive1",
                createBlurTransformed(16, null, null),
                "disruptive2"
            ).getBlurTransformed()
        )
    }
}