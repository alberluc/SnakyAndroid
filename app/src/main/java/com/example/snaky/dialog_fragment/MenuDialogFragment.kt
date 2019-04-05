package com.example.snaky.dialog_fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Button
import com.example.snaky.Game

import com.example.snaky.R

class MenuDialogFragment : DialogFragment() {

    var replayBtn: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.menu, null)
        replayBtn = view.findViewById(R.id.replay)
        replayBtn?.setOnClickListener {
            this.dismiss()
            Game.setState(Game.STATE_INIT)
            Game.setState(Game.STATE_RUN)
        }
        builder.setView(view)
        return builder.create()
    }
}