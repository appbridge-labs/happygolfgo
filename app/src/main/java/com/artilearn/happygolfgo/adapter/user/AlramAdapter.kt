package com.artilearn.happygolfgo.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.Alram
import com.artilearn.happygolfgo.util.convertTime

class AlramAdapter : RecyclerView.Adapter<AlramAdapter.AlramViewHolder>() {

    private val list: ArrayList<Alram> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlramViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_alram_message, parent, false
        )

        return AlramViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlramViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class AlramViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val date = v.findViewById<TextView>(R.id.tv_date)
        val title = v.findViewById<TextView>(R.id.tv_title)
        val content = v.findViewById<TextView>(R.id.tv_content)

        fun bind(item: Alram) {
            val time = convertTime(item.createdAt).trim().split(":")
            date.text = "${time[0]}.${time[1]}.${time[2]} ${time[3]}:${time[4]}"
            title.text = item.title
            content.text = item.body
        }
    }

    fun setItem(item: List<Alram>) {
        this.list.addAll(item)
        notifyDataSetChanged()
    }

    fun itemClear() {
        this.list.clear()
    }
}