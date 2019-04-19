package com.example.snaky.dialog_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.Button
import com.example.snaky.R
import com.example.snaky.activites.MainActivity

@SuppressLint("ValidFragment")
class RulesDialogFragment(val activity: MainActivity): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.rules, null)

        val btnContinue = view.findViewById(R.id.btn_continue) as Button
        btnContinue.setOnClickListener {
            this.dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}