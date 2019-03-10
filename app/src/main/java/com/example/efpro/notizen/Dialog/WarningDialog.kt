package com.example.efpro.notizen.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.R

class WarningDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Do you want to discard this note?")
            .setPositiveButton("YES") { dialog, id ->
                navigate.fragmentControl=navigate.fragmentRequested
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

