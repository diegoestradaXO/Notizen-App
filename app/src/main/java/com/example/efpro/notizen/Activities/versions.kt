package com.example.efpro.notizen.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.CalendarView
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efpro.notizen.Adapters.DateAdapter
import com.example.efpro.notizen.Adapters.NoteAdapter
import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_versions.*
import kotlinx.android.synthetic.main.activity_view_note.*
import java.text.SimpleDateFormat
import java.util.*

class versions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_versions)


        val idToExtract = getIntent().getStringExtra("identificador")
        val correo = getIntent().getStringExtra("correo")
        val titulo = getIntent().getStringExtra("titulo")
        val descripcion = getIntent().getStringExtra("descripcion")

        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))
        val adapter = DateAdapter()
        val listData = NoteViewModel.versions
        adapter.submitList(listData)//Submit data to recycler view
        recycler_view.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


        }
        ).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : DateAdapter.OnItemClickListener {
            override fun onItemClick(element: MutableList<String>) {
                val intent = Intent(applicationContext, ViewNoteActivity::class.java)
                intent.putExtra("identificador", idToExtract)
                intent.putExtra("content", element[1])
                intent.putExtra("correo", correo)
                intent.putExtra("titulo", titulo)
                intent.putExtra("descripcion", descripcion)
                startActivity(intent)
            }
        })

        val buttonHome = findViewById(R.id.buttonHome) as FloatingActionButton
        buttonHome.setOnClickListener {
            this.finish()
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val fecha = dayOfMonth.toString() + "/" + (month+1).toString() + "/" + year.toString()
            val newList = mutableListOf<MutableList<String>>()
            for (version in NoteViewModel.versions) {
                if(version[0]==fecha){
                    newList.add(version)
                }
            }
            adapter.submitList(newList)//Submit data to recycler view
            recycler_view.adapter = adapter
        }
    }
}
