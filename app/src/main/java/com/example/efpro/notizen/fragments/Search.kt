package com.example.efpro.notizen.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.Adapters.NoteAdapter

import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_navegate.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Search.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Search : androidx.fragment.app.Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
            // Inflate the layout for this fragment
        val reference = FirebaseDatabase.getInstance().getReference("notes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*, *>
                NoteViewModel.Notes = mutableListOf()
                val it = nota.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    var nombre = ""
                    for (i in NoteViewModel.allUsers){
                        if(i.id== currentNote.get("userid").toString()){
                            nombre = i.email
                        }
                    }
                    val nota = Nota(currentNote.get("nombre") as String,
                        currentNote.get("descripcion") as String,
                        currentNote.get("etiquetas") as List<String>,
                        currentNote.get("versiones") as List<List<String>>,
                        currentNote.get("privacidad") as String,
                        nombre
                    )
                    NoteViewModel.Notes.add(nota)
                }
            }

        })
        val recycler_view = view!!.findViewById(R.id.recycler_view) as RecyclerView
        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        recycler_view.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.HORIZONTAL))
        val adapter = NoteAdapter()
        adapter.submitList(NoteViewModel.Notes.shuffled())
        recycler_view.adapter = adapter

        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Nota) {
                //var intent = Intent(activity, EditNoteActivity::class.java)

                //startActivity(intent)
            }
        })

        return view
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
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
         * @return A new instance of fragment Search.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Search().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}
