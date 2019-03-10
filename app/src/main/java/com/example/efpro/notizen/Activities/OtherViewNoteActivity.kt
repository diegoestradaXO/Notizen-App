package com.example.efpro.notizen.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.efpro.notizen.R
import kotlinx.android.synthetic.main.activity_other_view_note.*

class OtherViewNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_view_note)
        val mail=getIntent().getStringExtra("correo")
        val titulo = getIntent().getStringExtra("titulo")
        val description = getIntent().getStringExtra("descripcion")
        val contenido = getIntent().getStringExtra("content")
        buttonHome.setOnClickListener{
            this.finish()
        }
        buttonShare.setOnClickListener{
            //Broadcast Reciever
        }

        tittle.text = titulo
        correo.text = mail
        descripcion.text = description
        content.text = contenido


    }
}
