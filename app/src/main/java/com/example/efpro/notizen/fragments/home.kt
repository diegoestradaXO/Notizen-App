package com.example.efpro.notizen.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.efpro.notizen.Activities.LoginActivity
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.Activities.navigate.Companion.auth

import com.example.efpro.notizen.R
import com.example.efpro.notizen.R.layout.fragment_home
import com.example.efpro.notizen.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_emailpassword.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.sign

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
// ...

   // private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            auth = FirebaseAuth.getInstance()
        }
        database = FirebaseDatabase.getInstance().reference

    }
    /*
    val postListener: ValueEventListener = ValueEventListener{
        override fun onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            Nota post = dataSnapshot.getValue(Nota.class);
            // ...
        }

        override fun onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
    mPostReference.addValueEventListener(postListener);
*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btn: Button = view.findViewById(R.id.signout)
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
