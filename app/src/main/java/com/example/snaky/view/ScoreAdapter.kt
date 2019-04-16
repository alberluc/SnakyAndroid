package com.example.snaky.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.snaky.R
import com.example.snaky.core.Score

class ScoreAdapter(
    context: Context,
    resource: Int,
    dataSource: ArrayList<Score>
)   : ArrayAdapter<Score>(context, resource, dataSource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.score_row, parent, false)

        val userText = rowView.findViewById(R.id.user_text) as TextView
        userText.text = getItem(position).user

        val valueText = rowView.findViewById(R.id.value_text) as TextView
        valueText.text = getItem(position).value.toString()

        val userPos = rowView.findViewById(R.id.user_pos) as TextView
        userPos.text = if (position + 1 > 1) "${position + 1}ème" else "1er"

        return rowView
    }

}