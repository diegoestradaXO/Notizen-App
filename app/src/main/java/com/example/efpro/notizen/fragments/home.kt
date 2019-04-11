package com.example.efpro.notizen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.*
import com.example.efpro.notizen.Activities.EditUser
import com.example.efpro.notizen.Activities.LoginActivity
import com.example.efpro.notizen.Activities.ViewNoteActivity
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.Activities.navigate.Companion.auth
import com.example.efpro.notizen.Adapters.NoteAdapter
import com.example.efpro.notizen.Dialog.WarningDeleteDialog

import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.fragment_home.*
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
    private lateinit var listData: List<Nota>
    private lateinit var adapter: NoteAdapter

// ...

   // private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            auth = FirebaseAuth.getInstance()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var editText = view.findViewById(R.id.buscarEdit) as EditText
        val botonBuscar = view.findViewById(R.id.botonBuscar) as ImageButton
        val btn: FloatingActionButton = view.findViewById(R.id.signout)
        val edit: FloatingActionButton = view.findViewById(R.id.editbutton)


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
                    //val user = User(
                    //    currentUser.get("id") as String,
                     //   currentUser.get("email") as String,
                    //    currentUser.get("biografia") as String,
                    //    currentUser.get("nombre") as String,
                     //   currentUser.get("seguidores") as List<String>,
                    //    currentUser.get("seguidos") as List<String>
                    //)
                    //if(idToExtract==user.id){
                    //    val descripcion:TextView = view.findViewById(R.id.descripcion)
                     //   descripcion.text = user.biografia
                    //}
                    //NoteViewModel.allUsers.add(user)
                }
            }

        })
        val reference = FirebaseDatabase.getInstance().getReference("users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val user =p0.getValue() as HashMap<*, *>
                val it = user.keys.iterator()//We iterate the hash
                var contador = 0
                while(it.hasNext()){
                    contador++
                    val key = it.next()
                    val currentUser = user.get(key) as HashMap<*,*>
                    Log.d("text" ,contador.toString() + currentUser.toString())
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
                        val description: TextView = view.findViewById(R.id.descripcion)
                        description.text = user.biografia
                    }
                }
            }

        })
        val recycler_view = view!!.findViewById(R.id.recycler_view) as RecyclerView
        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        recycler_view.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        val adapter = NoteAdapter()
        listData =  NoteViewModel.allNotes
        //Aqui agregar, If el query esta vacio, agregar el ListData, si no, agregar la que es con parametro
        adapter.submitList(listData)


        recycler_view.adapter = adapter

        botonBuscar.setOnClickListener{
            val parameter = editText!!.text.toString()
            if (parameter == ""){
                adapter.submitList(NoteViewModel.allNotes)
                recycler_view.adapter = adapter
            }else{
                val filteredList = NoteViewModel.allNotes.filter {it.etiquetas.contains(parameter)}
                adapter.submitList(filteredList)
                recycler_view.adapter = adapter
            }

        }
        /*Here it ends*/

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                warningDeleteDialog(listData.get(viewHolder.adapterPosition))
            }
        }
        ).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Nota) {
                val intent = Intent(activity, ViewNoteActivity::class.java)
                intent.putExtra("identificador",note.nombre+ navigate.auth.currentUser!!.uid)
                intent.putExtra("content", note.versiones!![note.versiones!!.size-1][0])
                intent.putExtra("correo",note.userid)
                intent.putExtra("titulo",note.nombre)
                intent.putExtra("descripcion",note.descripcion)
                startActivity(intent)
            }
        })
        btn.setOnClickListener(this)
        edit.setOnClickListener(this)
        return view
    }


    fun warningDeleteDialog(get: Nota) {
        val warningDeleteDialog = WarningDeleteDialog(get)
        warningDeleteDialog.show(this.fragmentManager!!,"example dialog")
    }



    override fun onClick(v: View?) = when (v?.id) {
        R.id.signout -> {
            navigate.auth.signOut()
            val intento = Intent(activity, LoginActivity::class.java)//Redirigimos a contactos
            startActivity(intento)
            activity!!.finish()
        }
        R.id.editbutton-> {
            val intento = Intent(activity,  EditUser::class.java)//Redirigimos a contactos
            startActivity(intento)
        }
        else -> {
        }

    }

    //fun filterList(): List<Nota>{
      //  var filteredList: MutableList<Nota> = mutableListOf<Nota>()
        //listData.forEach{
          //  if(it.etiquetas.contains(searchView.query)){
            //    filteredList.add(it)
            //}
        //}
        //return filteredList
    //}

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
