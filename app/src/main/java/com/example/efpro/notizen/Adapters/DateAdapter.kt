package com.example.efpro.notizen.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.vesion_item.view.*

class DateAdapter : ListAdapter<MutableList<String>, DateAdapter.DateHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MutableList<String>>() {
            override fun areItemsTheSame(oldItem: MutableList<String>, newItem: MutableList<String>): Boolean {
                return (oldItem[0]  == newItem[0] && oldItem[1] == newItem[1])//This doesn´t allow the same user to create a note with the same name two times
            }

            override fun areContentsTheSame(oldItem: MutableList<String>, newItem: MutableList<String>): Boolean {
                return oldItem[0]  == newItem[0] && oldItem[1] == newItem[1]
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.vesion_item, parent, false)
        return DateHolder(itemView)
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        val currentNote: MutableList<String> = getItem(position)
        holder.textViewDate.text = currentNote[0]
    }

    fun getNotaAt(position: Int): MutableList<String> {
        return getItem(position)
    }

    inner class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var textViewDate: TextView = itemView.date
    }

    interface OnItemClickListener {
        fun onItemClick(note: MutableList<String>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}