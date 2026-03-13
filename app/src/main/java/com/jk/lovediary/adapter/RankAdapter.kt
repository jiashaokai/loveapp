package com.jk.lovediary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.model.RecordTopVO

class RankAdapter(private val list: List<RecordTopVO>) :
    RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val rankText: TextView = view.findViewById(R.id.rankText)
        val nameText: TextView = view.findViewById(R.id.nameText)
        val countText: TextView = view.findViewById(R.id.countText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rank, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.rankText.text = when(position) {
            0 -> "🥇"
            1 -> "🥈"
            2 -> "🥉"
            else -> position.toString()
        }

        holder.nameText.text = item.userName
        holder.countText.text = "${item.userCount}次"
    }
}