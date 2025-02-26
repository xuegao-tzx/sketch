package com.github.panpf.sketch.test.utils

import com.github.panpf.sketch.Image
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.ImageResult
import com.github.panpf.sketch.request.RequestContext
import com.github.panpf.sketch.request.internal.OneShotRequestDelegate
import com.github.panpf.sketch.request.internal.OneShotRequestManager
import com.github.panpf.sketch.request.internal.RequestDelegate
import com.github.panpf.sketch.request.internal.RequestManager
import com.github.panpf.sketch.target.Target
import com.github.panpf.sketch.util.Size
import kotlinx.coroutines.Job

fun ImageRequest.toRequestContext(sketch: Sketch, size: Size): RequestContext {
    return RequestContext(sketch, this, size)
}

suspend fun ImageRequest.toRequestContext(sketch: Sketch): RequestContext {
    return RequestContext(sketch, this)
}

inline fun ImageRequest.Builder.target(
    crossinline onStart: (sketch: Sketch, request: ImageRequest, placeholder: Image?) -> Unit = { _, _, _ -> },
    crossinline onError: (sketch: Sketch, request: ImageRequest, error: ImageResult.Error, image: Image?) -> Unit = { _, _, _, _ -> },
    crossinline onSuccess: (sketch: Sketch, request: ImageRequest, result: ImageResult.Success, image: Image) -> Unit = { _, _, _, _ -> },
) = target(object : Target {

    private val requestManager = OneShotRequestManager()

    override fun getRequestManager(): RequestManager = requestManager

    override fun newRequestDelegate(
        sketch: Sketch,
        initialRequest: ImageRequest,
        job: Job
    ): RequestDelegate = OneShotRequestDelegate(sketch, initialRequest, this, job)

    override fun onStart(sketch: Sketch, request: ImageRequest, placeholder: Image?) =
        onStart(sketch, request, placeholder)

    override fun onSuccess(
        sketch: Sketch,
        request: ImageRequest,
        result: ImageResult.Success,
        image: Image
    ) = onSuccess(sketch, request, result, image)

    override fun onError(
        sketch: Sketch,
        request: ImageRequest,
        error: ImageResult.Error,
        image: Image?
    ) = onError(sketch, request, error, image)

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
})