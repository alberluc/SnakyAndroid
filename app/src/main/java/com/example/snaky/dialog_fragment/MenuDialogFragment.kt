package com.example.snaky.dialog_fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.*
import com.example.snaky.core.Game

import com.example.snaky.R
import com.example.snaky.activites.MainActivity
import com.example.snaky.core.Score
import com.example.snaky.core.Snake
import com.example.snaky.services.ScoreService
import com.example.snaky.view.ScoreAdapter

class MenuDialogFragment() : DialogFragment() {

    var replayBtn: Button? = null
    var scoreList: ListView? = null
    var scoreBtn: Button? = null
    var scoreDataSource = ArrayList<Score>()

    var scoreAdapter: ArrayAdapter<Score>? = null

    var activity: MainActivity? = null

    @SuppressLint("ValidFragment")
    constructor(activity: MainActivity): this() {
        this.activity = activity
    }

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

        scoreList = view.findViewById(R.id.score_list)
        scoreAdapter = ScoreAdapter(context!!, R.layout.score_row, scoreDataSource)
        scoreList!!.adapter = scoreAdapter

        val scoreUserValue = view.findViewById(R.id.score_user_value) as TextView
        scoreUserValue.text = Snake.getHeadPositions().count().toString()

        val userName = view.findViewById(R.id.user_text) as EditText

        scoreBtn = view.findViewById(R.id.valid_score)
        scoreBtn?.setOnClickListener {
            activity?.addScore(Score(userName.text.toString(), Snake.getHeadPositions().count()))
        }

        builder.setView(view)
        return builder.create()
    }
}