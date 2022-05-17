package com.task.productcatalog.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.task.productcatalog.R

fun Context.getImageUrl(name: String): String {
    return getString(R.string.link_image, name)
}

fun Context.getLargeImageUrl(name: String): String {
    return getString(R.string.link_large_image, name)
}

fun loadImage(
    context: Context,
    image: String,
    imageView: ImageView,
    requestListener: RequestListener<Drawable>? = null
) {
    Glide.with(context)
        .load(image)
        .fitCenter()
        .listener(requestListener)
        .into(imageView)
}

class ImageVisibilityRequestListener(private val imageView: ImageView) : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        imageView.visibility = View.GONE
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        imageView.visibility = View.VISIBLE
        return false
    }
}
