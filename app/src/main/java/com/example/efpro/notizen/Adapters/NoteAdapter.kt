package com.example.efpro.notizen.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.Nota
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity

class NoteAdapter : ListAdapter<Nota, NoteAdapter.NotaHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Nota>() {
            override fun areItemsTheSame(oldItem: Nota, newItem: Nota): Boolean {
                return (oldItem.nombre == newItem.nombre && oldItem.userid==newItem.userid)//This doesn´t allow the same user to create a note with the same name two times
            }

            override fun areContentsTheSame(oldItem: Nota, newItem: Nota): Boolean {
                return oldItem.nombre == newItem.nombre && oldItem.descripcion == newItem.descripcion
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NotaHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaHolder, position: Int) {
        val currentNote: Nota = getItem(position)
        holder.textViewNombre.text = currentNote.userid
        holder.textViewTitle.text = currentNote.nombre
        holder.textViewDescription.text = currentNote.descripcion
    }

    fun getNotaAt(position: Int): Nota {
        return getItem(position)
    }

    inner class NotaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var textViewTitle: TextView = itemView.titulo
        var textViewNombre: TextView = itemView.nombre
        var textViewDescription: TextView = itemView.descripcion
    }

    interface OnItemClickListener {
        fun onItemClick(note: Nota)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}