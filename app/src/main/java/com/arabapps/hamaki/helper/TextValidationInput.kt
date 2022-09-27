package com.sasco.user.helper

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import com.arabapps.hamaki.R


class TextValidationInput {


    companion object {
        fun isEditTextValid(editText: TextView): Boolean {
            if (editText.text.toString().trim().isEmpty()) {
                editText.setError(editText.context.resources.getString(R.string.reqiured))
//                AnimationHelper.animate(Techniques.Shake, editText)
                return false
            }
            return true
        }

        fun isEditTextValid(editText: EditText): Boolean {
            if (editText.text.toString().trim().isEmpty()) {
                editText.setError(editText.context.resources.getString(R.string.reqiured))
                return false
            }
            return true
        }

        fun isEmailValid(editText: EditText, context: Context): Boolean {
            if (!isEditTextValid(editText))
                return false
            if (
                !Patterns.EMAIL_ADDRESS.matcher(editText.text.toString().trim()).matches()
            ) {
//                editText.setError(editText.context.resources.getString(R.string.invalid_email))
//                AnimationHelper.animate(Techniques.Shake, editText)

                editText.setError(
                    context.resources.getString(R.string.invalid_email)
                )
                return false
            }
            return true
        }

        fun isPhoneValid(editText: EditText): Boolean {
            if (!isEditTextValid(editText))
                return false
            if (!Patterns.PHONE.matcher(editText.text.toString().trim()).matches()
            ) {
//                AnimationHelper.animate(Techniques.Shake, editText)

                editText.setError(
                    editText.context.resources.getString(R.string.phone_not_valid)
                )
                return false
            }
            return true
        }

        fun isPasswordalid(editText: EditText): Boolean {
            return true

            /*if (!isEditTextValid(editText))
                return false
            val pattern: Pattern
            val matcher: Matcher

//                "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(".{6,20}")
            matcher = pattern.matcher(editText.text.toString())
            if (!matcher.matches()) {

                editText.setError(
                    editText.context.resources.getString(R.string.password_weak)
                )
                return false
            }
            return true*/
        }
    }
}