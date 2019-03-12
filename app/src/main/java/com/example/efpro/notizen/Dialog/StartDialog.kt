package com.example.efpro.notizen.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.efpro.notizen.Activities.OtherViewNoteActivity
import com.example.efpro.notizen.Activities.navigate

class StartDialog : AppCompatDialogFragment() {
    public var content: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Do you want to discard this note?")
            .setPositiveButton("YES") { dialog, id ->
                val intent = Intent(activity, navigate::class.java)
                intent.putExtra("content", content)
                startActivity(intent)
                activity!!.finish()
            }
        // Create the AlertDialog object and return it
        return builder.create()
    }
}