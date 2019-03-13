package com.example.efpro.notizen.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.efpro.notizen.R
import com.example.efpro.notizen.R.id.correo
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_user.*



class EditUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        //listas para seguidores

        var followers : List<String>?= null
        var following : List<String>?= null

        //Create reference to firebase DataBase
        val referencia = FirebaseDatabase.getInstance().getReference("users")

        referencia.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val user =p0.getValue() as HashMap<*, *>
                val it = user.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentUser = user.get(key) as HashMap<*,*>
                    val idToExtract =navigate.auth.currentUser!!.uid
                    val user = User(
                        currentUser.get("id") as String,
                        currentUser.get("email") as String,
                        currentUser.get("biografia") as String,
                        currentUser.get("nombre") as String,
                        currentUser.get("seguidores") as List<String>,
                        currentUser.get("seguidos") as List<String>
                    )
                    followers = currentUser.get("seguidores") as List<String>
                    following = currentUser.get("seguidos") as List<String>
                    if(idToExtract==user.id){
                        val followers : TextView = findViewById(R.id.followers)
                        val following: TextView = findViewById(R.id.following)

                        val descripcion: TextView = findViewById(R.id.biografia)
                        followers.text = user.seguidores.size.toString()
                        following.text = user.seguidos.size.toString()
                        descripcion.text = user.biografia
                        //put the values in space
                        nombre.setText(user.nombre)
                        biografia.setHint(user.biografia)
                        correo.setText(user.email)
                    }
                }
            }

        })

        //Save button, it saves the changes made by the user

        save.setOnClickListener{
            //Create a reference to the DB
            val database = FirebaseDatabase.getInstance().reference

            //reference to users db and set new Values.
            database.child("users").child(navigate.auth.currentUser!!.uid).child("nombre").setValue(nombre.text.toString())
            database.child("users").child(navigate.auth.currentUser!!.uid).child("biografia").setValue(biografia.text.toString())

            //moves to navigate activity
            val intento = Intent(this, navigate::class.java)//Redirigimos a contactos
            startActivity(intento)
        }

    }


}
