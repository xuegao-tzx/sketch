# StateImage

Translations: [简体中文](state_image.zh.md)

[StateImage] is used to provide images for loading status and error status. There are several
implementations:

For View:

* [DrawableStateImage]: Use Drawable as status picture
* [ColorDrawableStateImage]: Create a ColorDrawable using colors as status picture
* [IconDrawableStateImage]: Use [IconDrawable] as status picture. It can ensure that the size of the
  icon remains unchanged and is not affected by the scaling of the component. It is suitable for use
  in waterfall layouts.
* [IconAnimatableDrawableStateImage]: Use [IconAnimatableDrawable] as state image. It can ensure that
  the size of the icon remains unchanged and is not affected by the scaling of the component. It is
  suitable for use in waterfall layouts.

For Compose:

* [PainterStateImage]: Use Painter as status picture
* [ColorPainterStateImage]: Create a ColorPainter using colors as a status picture
* [IconPainterStateImage]: Use [IconPainter] as status image. It can ensure that the size of the icon
  remains unchanged and is not affected by the scaling of the component. It is suitable for use in
  waterfall layouts.
* [IconAnimatablePainterStateImage]: Use [IconAnimatablePainter] as state image. It can ensure that
  the size of the icon remains unchanged and is not affected by the scaling of the component. It is
  suitable for use in waterfall layouts.

Generic:

* [CurrentStateImage]: Use the component's current Image as the state image
* [MemoryCacheStateImage]: Use the given memory cache key to obtain the Image from the memory cache
  as the status image, and use crossfade to achieve a perfect transition from small to large images.
* [ThumbnailMemoryCacheStateImage]: A simplified version of [MemoryCacheStateImage] that uses the
  given or currently requested uri to match the aspect ratio of the image in the memory cache to be
  consistent with the original image, and the thumbnail that has not been modified by Transformation
  is used as the state image. It can also be used with crossfade to achieve a perfect transition
  from small images to large images.
* [ConditionStateImage]: Different status pictures can be used according to different conditions

## Configuration

[StateImage] is used in the placeholder(), fallback(), and error() methods of [ImageRequest]
and [ImageOptions], as follows:

```kotlin
// View
ImageRequest(context, "https://example.com/image.jpg") {
  placeholder(R.drawable.placeholder)
  placeholder(context.getEquitableDrawable(R.drawable.placeholder))
  placeholder(IntColorDrawableStateImage(Color.Gray))
  placeholder(DrawableStateImage(R.drawable.placeholder))
  placeholder(
    IconDrawableStateImage(
      icon = R.drawable.placeholder,
      background = IntColorFetcher(Color.GRAY)
    )
  )
  placeholder(ConditionStateImage(defaultResId = R.drawable.error){
    addState(condition = MyCondition, resId = R.drawable.mystate)
  })

  fallback(R.drawable.fallback)
  fallback(context.getEquitableDrawable(R.drawable.fallback))
  fallback(IntColorDrawableStateImage(Color.RED))
  fallback(DrawableStateImage(R.drawable.fallback))
  fallback(
    IconDrawableStateImage(
      icon = R.drawable.fallback,
      background = IntColorFetcher(Color.RED)
    )
  )
  fallback(ConditionStateImage(defaultResId = R.drawable.error) {
    addState(condition = MyCondition, resId = R.drawable.mystate)
  })

  error(R.drawable.error)
  error(context.getEquitableDrawable(R.drawable.error))
  error(IntColorDrawableStateImage(Color.RED))
  error(DrawableStateImage(R.drawable.error))
  error(IconDrawableStateImage(icon = R.drawable.error, background = IntColorFetcher(Color.RED)))
  error(ConditionStateImage(defaultResId = R.drawable.error) {
    addState(condition = MyCondition, resId = R.drawable.mystate)
  })
}

// Compose
ComposableImageRequest("https://example.com/image.jpg") {
  placeholder(Res.drawable.placeholder)
  placeholder(rememberPainterStateImage(Res.drawable.placeholder))
  placeholder(rememberColorPainterStateImage(Color.Gray))
  placeholder(rememberIconPainterStateImage(Res.drawable.placeholder, background = Color.Gray))
  placeholder(ComposableConditionStateImage(defaultImage = Res.drawable.placeholder){
    addState(condition = MyCondition, stateImage = Res.drawable.mystate)
  })

  fallback(Res.drawable.fallback)
  fallback(rememberPainterStateImage(Res.drawable.fallback))
  fallback(rememberColorPainterStateImage(Color.Red))
  fallback(rememberIconPainterStateImage(Res.drawable.fallback, background = Color.Red))
  fallback(ComposableConditionStateImage(defaultImage = Res.drawable.fallback){
    addState(condition = MyCondition, stateImage = Res.drawable.mystate)
  })

  error(Res.drawable.error)
  error(rememberPainterStateImage(Res.drawable.error))
  error(rememberColorPainterStateImage(Color.Red))
  error(rememberIconPainterStateImage(Res.drawable.error, background = Color.Red))
  error(ComposableConditionStateImage(defaultImage = Res.drawable.error){
    addState(condition = MyCondition, stateImage = Res.drawable.mystate)
  })
}
```

> [!TIP]
> You need to import the `sketch-compose-resources` module placeholder, fallback, and error to
> support the DrawableResource of compose resources.

### Customize

You can refer to the existing implementation of [StateImage]

### ConditionStateImage

[ConditionStateImage] supports returning different status images according to different condition.
You
can implement the [ConditionStateImage] .Condition interface to extend the new type, and then use
the
custom type through [ConditionStateImage] .Builder.addState(), as follows:

```kotlin
object MyCondition : ConditionStateImage.Condition {

    override fun accept(
        request: ImageRequest,
        throwable: Throwable?
    ): Boolean = throwable is IOException
}

// View
ImageRequest(context, "https://example.com/image.jpg") {
    error(ConditionStateImage(R.drawable.error) {
        addState(condition = MyCondition, stateImage = DrawableStateImage(R.drawable.mystate))
    })
}

// Compose
ComposableImageRequest(context, "https://example.com/image.jpg") {
    error(ComposableConditionStateImage(Res.drawable.error) {
        addState(condition = MyCondition, stateImage = DrawableStateImage(Res.drawable.mystate))
    })
}
```

### Icon*StateImage

In the waterfall flow layout, since the size of each item may be different, when all items use the
same placeholder, the placeholder will appear to be larger or smaller on the page due to the scaling
of the component.

For this situation, using Icon\*StateImage can perfectly solve the problem. Icon\*StateImage
consists of an icon and a background. The icon is not affected by component scaling. The icon always
remains a fixed size, so that all placeholders on the page look the same. the size of

### ThumbnailMemoryCacheStateImage

When jumping from the image list page to the image details page, we hope to use the thumbnail image
loaded on the list page as a placeholder image when the details page loads the large image.

In this way, in conjunction with `crossfade(fadeStart = false)`, when the large image is loaded, the
page will gradually change from a blurry image to a clear image, which will have a better effect.

[ThumbnailMemoryCacheStateImage] can help us find thumbnails from the memory cache very
conveniently, as follows:

```kotlin
ImageRequest(context, "https://example.com/image.jpg") {
    placeholder(ThumbnailMemoryCacheStateImage())
    crossfade(fadeStart = false)
}
```

[ThumbnailMemoryCacheStateImage] By default, the uri of the current [ImageRequest] will be used to
find thumbnails in the memory, but if the list page and the details page use different
uri, you need to actively specify the uri of the list page, as follows:

```kotlin
ImageRequest(context, "https://example.com/image.jpg") {
    placeholder(ThumbnailMemoryCacheStateImage("https://www.sample.com/image.jpg?widht=300"))
    crossfade(fadeStart = false)
}
```

> [!TIP]
> The standard for thumbnails is images with the same aspect ratio and without any Transformation
> modification.

[StateImage]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/state/StateImage.kt

[ColorDrawableStateImage]: ../sketch-core/src/androidMain/kotlin/com/github/panpf/sketch/state/ColorDrawableStateImage.kt

[ColorPainterStateImage]: ../sketch-compose-core/src/commonMain/kotlin/com/github/panpf/sketch/state/ColorPainterStateImage.kt

[ConditionStateImage]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/state/ConditionStateImage.common.kt

[DrawableStateImage]: ../sketch-core/src/androidMain/kotlin/com/github/panpf/sketch/state/DrawableStateImage.kt

[IconDrawableStateImage]: ../sketch-core/src/androidMain/kotlin/com/github/panpf/sketch/state/IconDrawableStateImage.kt

[IconAnimatableDrawableStateImage]: ../sketch-core/src/androidMain/kotlin/com/github/panpf/sketch/state/IconAnimatableDrawableStateImage.kt

[IconPainterStateImage]: ../sketch-compose-core/src/commonMain/kotlin/com/github/panpf/sketch/state/IconPainterStateImage.common.kt

[IconAnimatablePainterStateImage]: ../sketch-compose-core/src/commonMain/kotlin/com/github/panpf/sketch/state/IconAnimatablePainterStateImage.common.kt

[MemoryCacheStateImage]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/state/MemoryCacheStateImage.kt

[ThumbnailMemoryCacheStateImage]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/state/ThumbnailMemoryCacheStateImage.kt

[ImageRequest]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/request/ImageRequest.common.kt

[ImageOptions]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/request/ImageOptions.common.kt

[CurrentStateImage]: ../sketch-core/src/commonMain/kotlin/com/github/panpf/sketch/state/CurrentStateImage.kt

[PainterStateImage]: ../sketch-compose-core/src/commonMain/kotlin/com/github/panpf/sketch/state/PainterStateImage.kt

[IconPainter]: ../sketch-compose-core/src/commonMain/kotlin/com/github/panpf/sketch/painter/IconPainter.common.kt

[IconAnimatablePainter]: ../sketch-compose-core/src/commonMain/kotlin/com/github/panpf/sketch/painter/IconAnimatablePainter.common.kt

[IconAnimatableDrawable]: ../sketch-core/src/androidMain/kotlin/com/github/panpf/sketch/drawable/IconAnimatableDrawable.kt

[IconDrawable]: ../sketch-core/src/androidMain/kotlin/com/github/panpf/sketch/drawable/IconDrawable.kt
