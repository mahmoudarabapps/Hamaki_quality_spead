package com.sasco.user.helper

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.arabapps.hamaki.R
import java.util.*

class DialogHelper {

    companion object {

        fun pickHour(
            context: Context,
            title: String,
            listener: TimePickerDialog.OnTimeSetListener
        ) {
            val hourPicker = TimePickerDialog(context, listener, 10, 0, false)
            hourPicker.setTitle(title)
            hourPicker.show()
        }

        fun pickDate(
            context: Context,
            title: String,
            listener: DatePickerDialog.OnDateSetListener
        ) {
            val datePicker = DatePickerDialog(
                context,
                listener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePicker.setTitle(title)
            datePicker.show()
        }

        //
        fun printMessage(context: Context, message: String) {

            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = inflater.inflate(R.layout.toast_layout, null)



            (layout.findViewById(R.id.text) as TextView).text = message

            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 200)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }

        fun getLoading(context: Context): Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.loading)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)
            return dialog
        }
    }

}