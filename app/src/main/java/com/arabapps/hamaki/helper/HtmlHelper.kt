package com.arabapps.hamaki.helper

import android.text.Spanned
import androidx.core.text.HtmlCompat

class HtmlHelper {
    companion object {

        fun getString( text: String): Spanned {
            if (text.isNullOrEmpty() || text.contentEquals("null"))
                return HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_LEGACY)
            return HtmlCompat.fromHtml(text.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
}