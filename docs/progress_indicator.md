# Download progress indicator

Translations: [简体中文](progress_indicator.zh.md)

Sketch provides extended functions for view and Compose to display download progress, as follows:

![sample_progress_indicator.png](images/sample_progress_indicator.png)

Three styles are provided to choose from, as follows:

![sample_progress_drawable.png](images/sample_progress_drawable.png)

> [!TIP]
> They can also adjust color, size and behavior

## Compose

First install component

`${LAST_VERSION}`: [![Download][version_icon]][version_link] (Not included 'v')

```kotlin
implementation("io.github.panpf.sketch4:sketch-extensions-compose:${LAST_VERSION}")
```

Then use the progressIndicator() function to add a progress indicator

```kotlin
// val progressPainter = rememberMaskProgressPainter()
// val progressPainter = rememberSectorProgressPainter()
val progressPainter = rememberRingProgressPainter()
val state = rememberAsyncImageState()
AsyncImage(
    uri = "https://example.com/image.jpg",
    modifier = Modifier
        .size(200.dp)
        .progressIndicator(state, progressPainter),
    state = state,
    contentDescription = "",
)
```

> [!TIP]
> Compose version function is implemented by [ProgressIndicatorModifier]

### Custom indicator

You can inherit [AbsProgressPainter] to implement your own progress indicator, as follows:

```kotlin
class MyProgressPainter(
    private val maskColor: Color = Color(PROGRESS_INDICATOR_MASK_COLOR),
    hiddenWhenIndeterminate: Boolean = PROGRESS_INDICATOR_HIDDEN_WHEN_INDETERMINATE,
    hiddenWhenCompleted: Boolean = PROGRESS_INDICATOR_HIDDEN_WHEN_COMPLETED,
    stepAnimationDuration: Int = PROGRESS_INDICATOR_STEP_ANIMATION_DURATION,
) : AbsProgressPainter(
    hiddenWhenIndeterminate = hiddenWhenIndeterminate,
    hiddenWhenCompleted = hiddenWhenCompleted,
    stepAnimationDuration = stepAnimationDuration
), SketchPainter {

    override val intrinsicSize: Size = Size(200.0, 200.0)

    override fun DrawScope.drawProgress(drawProgress: Float) {
        // Draw your indicator
    }
}
```

Then use your own indicator like this:

```kotlin
val progressPainter = remember { MyProgressPainter() }
val state = rememberAsyncImageState()
AsyncImage(
    uri = "https://example.com/image.jpg",
    modifier = Modifier
        .size(200.dp)
        .progressIndicator(state, progressPainter),
    state = state,
    contentDescription = "",
)
```

## View

First install component

`${LAST_VERSION}`: [![Download][version_icon]][version_link] (Not included 'v')

```kotlin
implementation("io.github.panpf.sketch4:sketch-extensions-view:${LAST_VERSION}")
```

Then use the show*ProgressIndicator() function with [SketchImageView] to add a progress indicator

```kotlin
val sketchImageView = SketchImageView(context)

sketchImageView.showMaskProgressIndicator()
// or
sketchImageView.showSectorProgressIndicator()
// or
sketchImageView.showRingProgressIndicator()
```

> [!TIP]
> View version functionality is implemented by [ProgressIndicatorAbility]

### Custom indicator

You can extends [AbsProgressDrawable] to implement your own progress indicator, as follows:

```kotlin
class MyProgressDrawable(
    hiddenWhenIndeterminate: Boolean = PROGRESS_INDICATOR_HIDDEN_WHEN_INDETERMINATE,
    hiddenWhenCompleted: Boolean = PROGRESS_INDICATOR_HIDDEN_WHEN_COMPLETED,
    stepAnimationDuration: Int = PROGRESS_INDICATOR_DEFAULT_STEP_ANIMATION_DURATION,
) : AbsProgressDrawable(
    hiddenWhenIndeterminate = hiddenWhenIndeterminate,
    hiddenWhenCompleted = hiddenWhenCompleted,
    stepAnimationDuration = stepAnimationDuration
) {

    private val paint = Paint().apply {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            alpha = this@RingProgressDrawable.alpha
        }
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            colorFilter = this@RingProgressDrawable.colorFilter
        }
    }

    override fun drawProgress(canvas: Canvas, drawProgress: Float) {
        val bounds = bounds.takeIf { !it.isEmpty } ?: return
        canvas.withSave {
            // Draw your indicator
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated(
        "Deprecated in Java. This method is no longer used in graphics optimizations",
        ReplaceWith("")
    )
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int = 200

    override fun getIntrinsicHeight(): Int = 200

    override fun mutate(): ProgressDrawable {
        return this
    }
}
```

Then use your own indicator like this:

```kotlin
val sketchImageView = SketchImageView(context)
sketchImageView.showProgressIndicator(MyProgressDrawable())
```

[version_icon]: https://img.shields.io/maven-central/v/io.github.panpf.sketch4/sketch-singleton

[version_link]: https://repo1.maven.org/maven2/io/github/panpf/sketch4/

[SketchImageView]: ../sketch-extensions-view/src/main/kotlin/com/github/panpf/sketch/SketchImageView.kt

[ProgressIndicatorAbility]: ../sketch-extensions-view/src/main/kotlin/com/github/panpf/sketch/ability/MimeTypeLogoAbility.kt

[AbsProgressDrawable]: ../sketch-extensions-core/src/androidMain/kotlin/com/github/panpf/sketch/drawable/internal/AbsProgressDrawable.kt

[ProgressIndicatorModifier]: ../sketch-extensions-compose/src/commonMain/kotlin/com/github/panpf/sketch/ability/ProgressIndicatorModifier.kt

[AbsProgressPainter]: ../sketch-extensions-compose/src/commonMain/kotlin/com/github/panpf/sketch/painter/internal/AbsProgressPainter.kt