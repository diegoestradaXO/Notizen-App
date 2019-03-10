package com.example.efpro.notizen.Dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@SuppressLint("ValidFragment")
class WarningDeleteDialog @SuppressLint("ValidFragment") constructor(val get: Nota) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Do you want to discard this note?")
            .setPositiveButton("YES") { dialog, id ->
                val identificador = get.nombre + navigate.auth.currentUser!!.uid
                FirebaseDatabase.getInstance().reference.child("notes").child(identificador).setValue(null)
                Toast.makeText(activity, "Note Deleted!", Toast.LENGTH_SHORT).show()
                navigate.fragmentControl=0
                val intento = Intent(activity, navigate::class.java)//Redirigimos a contactos
                startActivity(intento)
                activity!!.finish()
            }
            .setNegativeButton("NO") { dialog, id ->
                // User cancelled the dialog
            }
        // Create the AlertDialog object and return it
        return builder.create()
    }
}
