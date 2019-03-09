package com.example.efpro.notizen.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.efpro.notizen.R
import java.lang.ClassCastException

class ExampleDialog : AppCompatDialogFragment() {
    private var tittle: EditText? = null
    private var description: EditText? = null
    private lateinit var listener: ExampleDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.add_dialog, null)
        builder.setView(view)
            .setTitle("Configuration")
            .setNegativeButton("CANCEL") { dialog, which -> }
            .setPositiveButton("SAVE") {dialog,which ->
                val titulo = tittle!!.getText().toString()
                val descripcion = description!!.getText().toString()
                listener.applyTexts(titulo,descripcion)
            }

        tittle = view.findViewById(R.id.title)
        description = view.findViewById(R.id.description)

        return builder.create()
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
