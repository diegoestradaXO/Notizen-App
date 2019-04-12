package com.example.efpro.notizen.Activities


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.efpro.notizen.Dialog.WarningDeleteDialog
import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view_note.*

class ViewNoteActivity : AppCompatActivity() {
    private var isOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        //Gets all the Extras from the previous activity
        val contenido = getIntent().getStringExtra("content")
        val id = getIntent().getStringExtra("identificador")
        val correo=getIntent().getStringExtra("correo")
        val titulo = getIntent().getStringExtra("titulo")
        val descripcion = getIntent().getStringExtra("descripcion")
        NoteViewModel.versions.clear()
        val btnShare: FloatingActionButton = findViewById(R.id.buttonShare)
        val btnVersions: FloatingActionButton = findViewById(R.id.buttonVersions)
        val menuBtn: FloatingActionButton = findViewById(R.id.menuView)
        val btnTrash: FloatingActionButton = findViewById(R.id.buttonTrash)
        val btnEdit: FloatingActionButton = findViewById(R.id.buttonEdit)

        menuBtn.setOnClickListener {
            if (isOpen){

                menuBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotatefoward))
                btnShare.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fabclose))
                btnVersions.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fabclose))
                btnShare.isClickable = false
                btnVersions.isClickable = false
                btnTrash.isClickable = false
                btnEdit.isClickable = false
                btnShare.visibility = View.INVISIBLE
                btnVersions.visibility = View.INVISIBLE
                btnTrash.visibility = View.INVISIBLE
                btnEdit.visibility = View.INVISIBLE
                isOpen = !isOpen
            }else{
                menuBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.backwardrotate))
                btnShare.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fabopen))
                btnVersions.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fabopen))
                btnTrash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fabopen))
                btnEdit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fabopen))
                btnShare.isClickable = true
                btnVersions.isClickable = true
                btnTrash.isClickable = true
                btnEdit.isClickable = true
                btnShare.visibility = View.VISIBLE
                btnTrash.visibility = View.VISIBLE
                btnEdit.visibility = View.VISIBLE
                btnVersions.visibility = View.VISIBLE
                isOpen = !isOpen

            }
        }
        val referencia = FirebaseDatabase.getInstance().getReference("notes")
        referencia.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                val note =p0.getValue() as HashMap<*, *>
                val it = note.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentnote = note.get(key) as HashMap<*,*>

                    if(id==currentnote.get("id") as String){
                        val versiones = currentnote.get("versiones") as List<List<String>>
                        for (version in versiones){
                            val currentVersion = mutableListOf<String>()
                            currentVersion.add(version[1])
                            currentVersion.add(version[0])
                            NoteViewModel.versions.add(currentVersion)
                        }
                    }
                }
            }

        })
        //Home button, ends the actual activity and goes to the last screen opened
        buttonHome.setOnClickListener{
            this.finish()
        }

        //Share button, (State: not working)
        buttonShare.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)
            //Making the intent for email
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, correo)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello:\n I have just found this note named $titulo, please read it:\n\n $contenido \n\n\n\n")
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                Toast.makeText(this,"Sending mail", Toast.LENGTH_LONG).show()
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this, "Unexpected Error", Toast.LENGTH_LONG).show()
            }
        }

        //Delete button
        buttonTrash.setOnClickListener{
            //Creates a reference to "notes" db in firebase
            val reference = FirebaseDatabase.getInstance().getReference("notes")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    //gets the note
                    val nota =p0.getValue() as HashMap<*, *>
                    val it = nota.keys.iterator()//We iterate the hash
                    while(it.hasNext()){
                        val key = it.next()
                        val currentNote = nota.get(key) as HashMap<*,*>
                        if(currentNote.get("id").toString() == id){
                            val nota = Nota(
                                currentNote.get("nombre") as String,
                                currentNote.get("descripcion") as String,
                                currentNote.get("etiquetas") as List<String>,
                                currentNote.get("versiones") as List<List<String>>,
                                currentNote.get("privacidad") as String,"x")
                            warningDeleteDialog(nota)
                        }
                    }
                }

            })
        }


        //Edit button, uses EditNoteActivity
        buttonEdit.setOnClickListener{
            val intent = Intent(this, EditNoteActivity::class.java)

            //Sends the actual info as Extra to the new Activity so the user can know what it needs to be changed.
            intent.putExtra("content", contenido)
            intent.putExtra("identificador",id)
            startActivity(intent)
            this.finish()
        }

        buttonVersions.setOnClickListener{
            val intent = Intent(this, versions::class.java)
            //Sends the actual info as Extra to the new Activity so the user can know what it needs to be changed.
            intent.putExtra("identificador",id)
            intent.putExtra("correo",correo)
            intent.putExtra("titulo",titulo)
            intent.putExtra("descripcion",descripcion)
            startActivity(intent)
            this.finish()

        }


        if(correo!=navigate.auth.currentUser!!.uid){
            val intent = Intent(this, OtherViewNoteActivity::class.java)
            intent.putExtra("content", contenido)
            intent.putExtra("titulo",titulo)
            intent.putExtra("correo",correo)
            intent.putExtra("descripcion",descripcion)
            intent.putExtra("identificador",id)
            startActivity(intent)
            this.finish()
        }
        val reference = FirebaseDatabase.getInstance().getReference("notes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*, *>
                val it = nota.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    if(currentNote.get("id").toString() == id){
                        val versiones = currentNote["versiones"] as List<List<String>>
                        var index = versiones.size - 1
                        val contenido = versiones[index]
                        tittle.setText(currentNote.get("nombre").toString())
                        descrip.setText(currentNote.get("descripcion").toString())
                    }
                }
            }

        })
        content.setText(contenido)
    }
    fun warningDeleteDialog(get: Nota) {
        val warningDeleteDialog = WarningDeleteDialog(get)
        warningDeleteDialog.show(this.supportFragmentManager,"example dialog")
    }
}
