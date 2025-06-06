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

package com.github.panpf.sketch.drawable

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.PorterDuff.Mode
import android.graphics.Rect
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.graphics.drawable.Drawable.Callback
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import com.github.panpf.sketch.util.Size
import com.github.panpf.sketch.util.calculateScaleMultiplierWithCrop
import com.github.panpf.sketch.util.calculateScaleMultiplierWithInside
import com.github.panpf.sketch.util.isNotEmpty
import com.github.panpf.sketch.util.toLogString
import kotlin.math.ceil
import kotlin.math.floor

/**
 * It consists of two parts: icon and bg. bg is scaled to fill bounds, the icon size is unchanged always centered.
 * It is suitable for use as a placeholder image for waterfall flow.
 *
 * @see com.github.panpf.sketch.core.android.test.drawable.IconDrawableTest
 */
open class IconDrawable constructor(
    val icon: Drawable,
    val background: Drawable? = null,
    val iconSize: Size? = null,
    @ColorInt val iconTint: Int? = null
) : Drawable(), Callback, SketchDrawable {

    init {
        if (iconSize != null) {
            require(!iconSize.isEmpty) {
                "iconSize must be not empty"
            }
        } else {
            require(icon.intrinsicWidth > 0 && icon.intrinsicHeight > 0) {
                "When iconSize is not set, icon's size must be not empty"
            }
        }
        background?.callback = this
        icon.callback = this
        iconTint?.let { DrawableCompat.setTint(icon, it) }
    }

    override fun draw(canvas: Canvas) {
        background?.draw(canvas)
        icon.draw(canvas)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        val containerWidth = bounds.width()
        val containerHeight = bounds.height()
        val background = background
        if (background != null) {
            val backgroundSize = Size(background.intrinsicWidth, background.intrinsicHeight)
            val backgroundBounds = if (backgroundSize.isNotEmpty) {
                val backgroundScaleFactor = calculateScaleMultiplierWithCrop(
                    srcWidth = backgroundSize.width.toFloat(),
                    srcHeight = backgroundSize.height.toFloat(),
                    dstWidth = containerWidth.toFloat(),
                    dstHeight = containerHeight.toFloat()
                )
                val scaledWidth = backgroundSize.width * backgroundScaleFactor
                val scaledHeight = backgroundSize.height * backgroundScaleFactor
                val left = bounds.left + (containerWidth - scaledWidth) / 2f
                val top = bounds.top + (containerHeight - scaledHeight) / 2f
                val right = left + scaledWidth
                val bottom = top + scaledHeight
                Rect(
                    /* left = */ floor(left).toInt(),
                    /* top = */ floor(top).toInt(),
                    /* right = */ ceil(right).toInt(),
                    /* bottom = */ ceil(bottom).toInt()
                )
            } else {
                bounds
            }
            background.bounds = backgroundBounds
        }

        val realIconSize = iconSize ?: Size(icon.intrinsicWidth, icon.intrinsicHeight)
        val backgroundScaleFactor = calculateScaleMultiplierWithInside(
            srcWidth = realIconSize.width.toFloat(),
            srcHeight = realIconSize.height.toFloat(),
            dstWidth = containerWidth.toFloat(),
            dstHeight = containerHeight.toFloat()
        )
        val scaledWidth = realIconSize.width * backgroundScaleFactor
        val scaledHeight = realIconSize.height * backgroundScaleFactor
        val left = bounds.left + (containerWidth - scaledWidth) / 2f
        val top = bounds.top + (containerHeight - scaledHeight) / 2f
        val right = left + scaledWidth
        val bottom = top + scaledHeight
        val iconBounds = Rect(
            /* left = */ floor(left).toInt(),
            /* top = */ floor(top).toInt(),
            /* right = */ ceil(right).toInt(),
            /* bottom = */ ceil(bottom).toInt()
        )
        icon.bounds = iconBounds
    }

    override fun getIntrinsicWidth(): Int {
        return -1
    }

    override fun getIntrinsicHeight(): Int {
        return -1
    }

    override fun setChangingConfigurations(configs: Int) {
        background?.changingConfigurations = configs
        icon.changingConfigurations = configs
    }

    override fun getChangingConfigurations(): Int {
        return icon.changingConfigurations
    }

    override fun isFilterBitmap(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.M
                && (icon.isFilterBitmap || background?.isFilterBitmap == true)
    }

    override fun setFilterBitmap(filter: Boolean) {
        background?.isFilterBitmap = filter
        icon.isFilterBitmap = filter
    }

    override fun setAlpha(alpha: Int) {
        background?.alpha = alpha
        icon.alpha = alpha
    }

    override fun getAlpha(): Int {
        return DrawableCompat.getAlpha(icon)
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        background?.colorFilter = colorFilter
        icon.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun setColorFilter(color: Int, mode: Mode) {
        @Suppress("DEPRECATION")
        background?.setColorFilter(color, mode)
        @Suppress("DEPRECATION")
        icon.setColorFilter(color, mode)
    }

    override fun getColorFilter(): ColorFilter? {
        return DrawableCompat.getColorFilter(icon)
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        @Suppress("DEPRECATION")
        return icon.opacity.takeIf { it != PixelFormat.OPAQUE }
            ?: background?.opacity.takeIf { it != PixelFormat.OPAQUE }
            ?: PixelFormat.OPAQUE
    }

    override fun isStateful(): Boolean {
        return background?.isStateful == true || icon.isStateful
    }

    override fun setState(stateSet: IntArray): Boolean {
        val result1 = background?.setState(stateSet) == true
        val result2 = icon.setState(stateSet)
        return result1 || result2
    }

    override fun getState(): IntArray {
        return icon.state
    }

    override fun jumpToCurrentState() {
        background?.jumpToCurrentState()
        icon.jumpToCurrentState()
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        super.setVisible(visible, restart)
        val result1 = background?.setVisible(visible, restart) == true
        val result2 = icon.setVisible(visible, restart)
        return result1 || result2
    }

    override fun getTransparentRegion(): Region? {
        return background?.transparentRegion ?: icon.transparentRegion
    }

    override fun getPadding(padding: Rect): Boolean {
        return background?.getPadding(padding) == true
    }

    override fun onLevelChange(level: Int): Boolean {
        val result1 = background?.setLevel(level) == true
        val result2 = icon.setLevel(level)
        return result1 || result2
    }

    override fun setAutoMirrored(mirrored: Boolean) {
        background?.let { DrawableCompat.setAutoMirrored(it, mirrored) }
        DrawableCompat.setAutoMirrored(icon, mirrored)
    }

    override fun isAutoMirrored(): Boolean {
        return background?.let { DrawableCompat.isAutoMirrored(it) } == true
                || DrawableCompat.isAutoMirrored(icon)
    }

    override fun setTint(tint: Int) {
        DrawableCompat.setTint(icon, tint)
    }

    override fun setTintList(tint: ColorStateList?) {
        DrawableCompat.setTintList(icon, tint)
    }

    override fun setTintMode(tintMode: Mode?) {
        DrawableCompat.setTintMode(icon, tintMode)
    }

    override fun setHotspot(x: Float, y: Float) {
        background?.let { DrawableCompat.setHotspot(it, x, y) }
        DrawableCompat.setHotspot(icon, x, y)
    }

    override fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) {
        background?.let { DrawableCompat.setHotspotBounds(it, left, top, right, bottom) }
        DrawableCompat.setHotspotBounds(icon, left, top, right, bottom)
    }

    override fun invalidateDrawable(who: Drawable) {
        invalidateSelf()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        scheduleSelf(what, `when`)
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        unscheduleSelf(what)
    }

    override fun mutate(): IconDrawable {
        val mutateIcon = icon.mutate()
        val mutateBackground = background?.mutate()
        return if (mutateIcon !== icon || mutateBackground !== background) {
            IconDrawable(
                icon = mutateIcon,
                background = mutateBackground,
                iconSize = iconSize,
                iconTint = iconTint
            )
        } else {
            this
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as IconDrawable
        if (icon != other.icon) return false
        if (background != other.background) return false
        if (iconSize != other.iconSize) return false
        return iconTint == other.iconTint
    }

    override fun hashCode(): Int {
        var result = icon.hashCode()
        result = 31 * result + (background?.hashCode() ?: 0)
        result = 31 * result + (iconSize?.hashCode() ?: 0)
        result = 31 * result + (iconTint?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "IconDrawable(" +
            "icon=${icon.toLogString()}, " +
            "background=${background?.toLogString()}, " +
            "iconSize=$iconSize, " +
            "iconTint=$iconTint" +
            ")"
}