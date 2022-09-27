package com.arabapps.hamaki.helper

import android.view.View
import android.widget.TextView

class TextViewHelper {
    companion object {
        fun TextView.setAndHide(text: String) {
            if (text.isNullOrEmpty())
                this.visibility = View.GONE
            else {
                this.visibility = View.VISIBLE
                this.text = text
            }
        }

        fun TextView.set(text: String) {
            if (text.isNullOrEmpty())
                this.text = ""
            else
                this.text = text
        }
    }
}