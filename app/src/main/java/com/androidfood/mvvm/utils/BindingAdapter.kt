package com.androidfood.mvvm.utils

import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.androidfood.mvvm.R

/**
 * Created by Daniyal Ahmed on 11/4/2020.
 */

@BindingAdapter("renderHtml")
fun bindRenderHtml(view: TextView, description: String?) {
    if (description != null) {
        view.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        view.movementMethod = LinkMovementMethod.getInstance()
    } else {
        view.text = ""
    }
}


@BindingAdapter("drawerImage")
fun setDrawerItemImage(view: ImageView, id: Int?) {
    when (id) {
        0 -> {
            view.background =
                ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        1 -> {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        2 -> {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        3 -> {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        4 -> {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        5 -> {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        6 -> {
            view.background =
                ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        7 -> {
            view.background =
                ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        8 -> {
            view.background =
                ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
        9 -> {
            view.background =
                ContextCompat.getDrawable(view.context, R.drawable.ic_edit_icon)
        }
    }
}