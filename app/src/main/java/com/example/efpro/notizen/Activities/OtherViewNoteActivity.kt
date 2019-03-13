package com.example.efpro.notizen.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_other_view_note.*

class OtherViewNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_view_note)

        //Gets all the Extras from the previous activity
        val mail=getIntent().getStringExtra("correo")
        val titulo = getIntent().getStringExtra("titulo")
        val description = getIntent().getStringExtra("descripcion")
        val contenido = getIntent().getStringExtra("content")
        val id = getIntent().getStringExtra("identificador")

        //Home button, ends the actual activity and goes to the last screen opened
        buttonHome.setOnClickListener{
            this.finish()
        }

        //Button for share note function (not working)
        buttonShare.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)
            //Making the intent for email
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, mail)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello:\n I have just found this note named $titulo, please read it: \n\n $contenido \n\n\n\n")
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                Toast.makeText(this,"Sending mail",Toast.LENGTH_LONG).show()
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this, "Unexpected Error", Toast.LENGTH_LONG).show()
            }
        }

        tittle.text = titulo
        descripcion.text = description
        content.text = contenido

        //Creates a reference to "users" db in firebase
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*, *>
                val it = nota.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    if(currentNote.get("id").toString() == mail){
                        correo.text = currentNote.get("nombre").toString() + " | " + currentNote.get("email").toString()
                    }
                }
            }

        })

    }
}
