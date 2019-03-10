package com.example.efpro.notizen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efpro.notizen.Activities.LoginActivity
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.Activities.navigate.Companion.auth
import com.example.efpro.notizen.Adapters.NoteAdapter

import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.concurrent.TimeoutException
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var auth: FirebaseAuth


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [home.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class home : androidx.fragment.app.Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var listData: MutableList<Nota>
// ...

   // private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            auth = FirebaseAuth.getInstance()
        }
        database = FirebaseDatabase.getInstance().getReference("notes")
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btn: Button = view.findViewById(R.id.signout)


        val reference = database
        val referencia = FirebaseDatabase.getInstance().getReference("users")
        referencia.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val user =p0.getValue() as HashMap<*, *>
                NoteViewModel.allUsers = mutableListOf()
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
                    if(idToExtract==user.id){
                        val followers :TextView= view.findViewById(R.id.followers)
                        val following:TextView=view.findViewById(R.id.following)
                        val descripcion:TextView = view.findViewById(R.id.descripcion)
                        followers.text = user.seguidores.size.toString()
                        following.text = user.seguidos.size.toString()
                        descripcion.text = user.biografia
                    }
                    NoteViewModel.allUsers.add(user)
                }
            }

        })

        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*, *>
                NoteViewModel.allNotes.clear()
                val it = nota.keys.iterator()//We iterate the hash
			    while(it.hasNext()){
				    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    if(currentNote.get("userid")== navigate.auth.currentUser!!.uid){
                        val nota = Nota(currentNote.get("nombre") as String,
                            currentNote.get("descripcion") as String,
                            currentNote.get("etiquetas") as List<String>,
                            currentNote.get("versiones") as List<List<String>>,
                            currentNote.get("privacidad") as String,
                            navigate.auth.currentUser!!.email!!
                        )
                        NoteViewModel.allNotes.add(nota)
                    }
			    }
            }

        })
        val recycler_view = view!!.findViewById(R.id.recycler_view) as RecyclerView
        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        recycler_view.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.HORIZONTAL))
        val adapter = NoteAdapter()
        adapter.submitList(NoteViewModel.allNotes.shuffled())
        recycler_view.adapter = adapter

        /*Here it ends*/



        btn.setOnClickListener(this)
        return view
    }





    override fun onClick(v: View?) = when (v?.id) {
        R.id.signout -> {
            navigate.auth.signOut()
            val intento = Intent(activity, LoginActivity::class.java)//Redirigimos a contactos
            startActivity(intento)
        }
        else -> {
        }
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/


}
