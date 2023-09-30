package com.example.apptesttmbd.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.apptesttmbd.R
import java.util.HashMap

class customDialog private constructor(){
    private var progressDialogs: HashMap<Activity, ProgressDialog?>? = null
        private get() {
            if (field == null) field = HashMap()
            return field
        }


    companion object {
        private val sync = Any()
        private var _userDialog: customDialog? = null
        val instance: customDialog?
            get() {
                if (_userDialog == null) {
                    synchronized(sync) { if (_userDialog == null) _userDialog = customDialog() }
                }
                return _userDialog
            }
    }
    fun DialogMessageConfirm(context: Activity, title:String, question: String, textYes:String?, useNO: Boolean, onYes: ()->Unit){

        val inflater: LayoutInflater = context.getLayoutInflater()
        val layout: View = inflater.inflate(R.layout.dialog_confirm, null)
        val builderEdit: AlertDialog = AlertDialog.Builder(context).create()
        builderEdit.setView(layout)
        builderEdit.setCancelable(false)
        layout.setBackgroundColor(context.getResources().getColor(android.R.color.transparent))
        builderEdit.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val t_title: TextView = layout.findViewById(R.id.lblTitleDialog)
        val t_question: TextView = layout.findViewById(R.id.lblQuestionDialog)
        t_title.text= title.toString() //context.getString(R.string.delete_attendande_title)
        t_question.text= question.toString() //context.getString(R.string.delete_attendance)
        val btnYes: Button = layout.findViewById(R.id.btn_yes_delete)
        val btnNo: Button = layout.findViewById(R.id.btn_no_delete)
        btnNo.visibility= View.GONE
        if(useNO){
            btnNo.visibility= View.VISIBLE
        }






        btnYes.setOnClickListener({
                dialog->onYes.invoke()
            builderEdit.dismiss()
        })

        btnNo.setOnClickListener {
            builderEdit.dismiss()
        }
        builderEdit.show()
    }
    fun DialogSimple(context: Activity, message: CharSequence?) {
        val inflater = context.layoutInflater
        val layout: View = inflater.inflate(R.layout.dialog_simple, null)
        val builderEdit = AlertDialog.Builder(context).create()
        builderEdit.setView(layout)

        layout.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
        builderEdit.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var lblMessage: TextView =layout.findViewById(R.id.lblMessageDialogSimple)
        lblMessage.text= message

        builderEdit.show()
        Handler().postDelayed({ builderEdit.dismiss() }, 4000)
    }
    fun showWaitDialog(context: Activity, title: CharSequence?): ProgressDialog? {
        return this.showWaitDialog(context, title, context.getString(R.string.wait))
    }

    fun showWaitDialog(
        activity: Activity,
        title: CharSequence?,
        message: CharSequence?
    ): ProgressDialog? {
        var title = title
        var message = message
        if (title == null || title.length == 0) title =
            activity.getResources().getString(R.string.app_name)
        if (message == null || message.length == 0) message =
            activity.getResources().getString(R.string.wait)
        var pDialog: ProgressDialog? = progressDialogs!![activity]
        if (pDialog != null) {
            pDialog.setTitle(title)
            pDialog.setMessage(message)
            if (!pDialog.isShowing()) pDialog.show()
        } else {
            pDialog = ProgressDialog(activity)
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            pDialog.setTitle(title)
            pDialog.setIcon(R.drawable.moviesicon)
            pDialog.setMessage(message)
            pDialog.setCanceledOnTouchOutside(false)
            pDialog.setMax(100)
            progressDialogs!![activity] = pDialog
            pDialog.show()
        }
        return pDialog
    }

    fun closeWaitdialog(activity: Activity): ProgressDialog? {
        var pDialog: ProgressDialog? = null
        if (progressDialogs!!.containsKey(activity)) {
            pDialog = progressDialogs!![activity]
            if (pDialog!!.isShowing()) {
                pDialog.dismiss()
            }
            progressDialogs!!.remove(activity)
        }
        return pDialog
    }


}