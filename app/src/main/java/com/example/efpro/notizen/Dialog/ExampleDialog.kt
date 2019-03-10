package com.example.efpro.notizen.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.R
import com.example.efpro.notizen.fragments.addNote
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ClassCastException
import java.text.SimpleDateFormat
import java.util.*

class ExampleDialog : AppCompatDialogFragment() {
    private var tittle: EditText? = null
    private var description: EditText? = null
    private var tags:EditText? =null
    private var swich1: Switch? = null
    private lateinit var listener: ExampleDialogListener
    public var content: String = "x"
    private lateinit var  mDatabase: DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mDatabase = FirebaseDatabase.getInstance().reference
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.add_dialog, null)

        builder.setView(view)
            .setTitle("Configuration")
            .setNegativeButton("CANCEL") { dialog, which -> }
            .setPositiveButton("SAVE") {dialog,which ->
                addNote.tittle = tittle!!.getText().toString()
                addNote.descripcion = description!!.getText().toString()
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                val versiones = listOf<String>("0",content,currentDate)
                val versiones1= listOf<List<String>>(versiones)
                writeNewNote(tittle!!.text.toString(), description!!.text.toString  (), swich1!!.isChecked,
                    tags!!.text.toString().split(",".toRegex()),versiones1)

            }

        tittle = view.findViewById(R.id.title)
        description = view.findViewById(R.id.description)
        swich1 = view.findViewById(R.id.switch1)
        tags = view.findViewById(R.id.tags)

        return builder.create()
    }

    private fun writeNewNote(
        nombre:String,
        descripcion:String,
        privacidad: Boolean,
        etiquetas:List<String>,
        versiones: List<List<String>>
    ) {
        val privacity = privacidad as String
        val user = navigate.auth.currentUser
        val note= Nota(nombre,descripcion,etiquetas,versiones,privacity, user!!.uid)
        mDatabase.child("notes").child(nombre).setValue(note)
        val intento = Intent(activity, navigate::class.java)//Redirigimos a contactos
        startActivity(intento)
        activity!!.finish()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ExampleDialogListener
        } catch (e: ClassCastException) {
            //throw ClassCastException(context.toString() + "must implement Example Integer")
        }
    }

    interface ExampleDialogListener {
        fun applyTexts(tittle: String, description: String)
    }


}
