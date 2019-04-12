package com.example.efpro.notizen.fragments

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.efpro.notizen.Activities.navigate
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.R.string.cancel
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.efpro.notizen.Activities.LoginActivity
import com.example.efpro.notizen.Dialog.ExampleDialog
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.add_dialog.*
import kotlinx.android.synthetic.main.fragment_add_note.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [addNote.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [addNote.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class addNote : androidx.fragment.app.Fragment(),View.OnClickListener,ExampleDialog.ExampleDialogListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database: DatabaseReference
    //private var listener: OnFragmentInteractionListener? = null

    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    lateinit var recordPermission: Array<String>
    var isOpen = false
    lateinit var image_uri: Uri


    companion object {
        var tittle = ""
        var descripcion = ""
        private val CAMERA_REQUEST_CODE = 200
        private val RECORD_REQUEST_CODE = 100
        private val STORAGE_REQUEST_CODE = 400
        private val IMAGE_PICK_CAMERA_CODE = 1001
        private val IMAGE_PICK_GALLERY_CODE = 1000
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        database = FirebaseDatabase.getInstance().reference



    }

    override fun applyTexts(tittle: String, description: String) {
        //do nothing
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var speechRecognize = SpeechRecognizer.createSpeechRecognizer(context)
        var speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognize.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
            }

            override fun onRmsChanged(p0: Float) {
            }

            override fun onBufferReceived(p0: ByteArray?) {
            }

            override fun onPartialResults(p0: Bundle?) {
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(p0: Int) {
            }

            override fun onResults(p0: Bundle?) {
                val matches = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if(matches != null) {
                    content.setText(matches.first())
                }
            }

        })

        // Inflate the layout for this fragment
        cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        recordPermission = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)
        val btn: FloatingActionButton = view.findViewById(R.id.buttonGuardar)
        val btnCamera: FloatingActionButton = view.findViewById(R.id.buttonCamara)
        val btnAudio: FloatingActionButton = view.findViewById(R.id.buttonAudio)
        val menuBtn: FloatingActionButton = view.findViewById(R.id.menu)
        btn.setOnClickListener(this)

        menuBtn.setOnClickListener {
            if (isOpen){

                menuBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotatefoward))
                btnCamera.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fabclose))
                btnAudio.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fabclose))
                btnCamera.isClickable = false
                btnAudio.isClickable = false
                btnCamera.visibility = View.INVISIBLE
                btnAudio.visibility = View.INVISIBLE
                isOpen = !isOpen
            }else{
                menu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.backwardrotate))
                buttonCamara.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fabopen))
                buttonAudio.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fabopen))
                btnCamera.isClickable = true
                btnAudio.isClickable = true
                btnCamera.visibility = View.VISIBLE
                btnAudio.visibility = View.VISIBLE

                isOpen = !isOpen

            }
        }
        btnCamera.setOnClickListener{
            showImageImportDialog()
        }
        if(navigate.contenido!=""){
            val content : EditText= view.findViewById(R.id.content)
            content.setText(navigate.contenido)
        }
        btnAudio.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_UP ->
                    if (checkRecordPermission())
                        speechRecognize.stopListening()
                MotionEvent.ACTION_DOWN -> {
                    if (checkRecordPermission())
                        speechRecognize.startListening(speechIntent)
                    else
                        permissionRecord()
                }
                else -> {

                }
            }
            false

        }
        return view
    }

    override fun onClick(v: View?) = when (v?.id) {
        R.id.buttonGuardar -> {
            openDialog()
        }
        else -> {
        }
    }

        fun openDialog(): Unit {
            val exampleDialog = ExampleDialog()
            exampleDialog.content=content.text.toString()
            exampleDialog.show(this.fragmentManager!!,"example dialog")
        }

    private fun showImageImportDialog() {
        //items to display in dialog
        val items = arrayOf(" Camera", " Gallery")
        val dialog = AlertDialog.Builder(activity)
        //set title
        dialog.setTitle("Selecciona opcion")
        dialog.setItems(items) { dialog, which ->
            if (which == 0) {
                //camera option clicked
                if (!checkCameraPermission()) {
                    //camera permission not allowed, request it
                    requestCameraPermission()
                    Toast.makeText(activity!!,"Permission were given. Try again!",Toast.LENGTH_LONG).show()
                } else {
                    //permission allowed, take picture
                    pickCamera()
                }
            }
            if (which == 1) {
                //gallery option
                if (!checkStoragePermission()) {
                    //camera permission not allowed, request it
                    requestStoragePermission()
                    Toast.makeText(activity!!,"Permission were given. Try again!",Toast.LENGTH_LONG).show()
                } else {
                    //permission allowed, take picture
                    pickGallery()
                }
            }
        }
        dialog.create().show()

    }

    private fun checkCameraPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        val result1 = ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(requireActivity(),
            this.cameraPermission,
            addNote.CAMERA_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            this.storagePermission,
            addNote.STORAGE_REQUEST_CODE)
    }

    private fun checkRecordPermission(): Boolean{
        val result =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun permissionRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.RECORD_AUDIO)) {

                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        addNote.RECORD_REQUEST_CODE)
                }

        }
    }
    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "NewPic") //Title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text") //description
        image_uri = activity!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, addNote.IMAGE_PICK_CAMERA_CODE)
    }

    private fun pickGallery() {
        //intent to pick from gallery
        val intent = Intent(Intent.ACTION_PICK)
        //set intent type
        intent.type = "image/*"
        startActivityForResult(intent, addNote.IMAGE_PICK_GALLERY_CODE)
    }

    //handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.size > 0) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && writeStorageAccepted) {
                    pickCamera()
                } else {
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            STORAGE_REQUEST_CODE -> if (grantResults.size > 0) {
                val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeStorageAccepted) {
                    pickGallery()
                } else {
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show()
                }

            }
            RECORD_REQUEST_CODE ->
                if (grantResults.size > 0) {
                    val audioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (audioAccepted) {
                    } else {
                        Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    //handle image result

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //got image from camera
        val myImage: ImageView = view!!.findViewById(R.id.imageViewP)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //got image from gallery now crop it
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(activity!!.applicationContext, this)// enable image guidelines

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //got image from camera now crop it
                CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(activity!!.applicationContext, this)// enable image guidelines

            }
        }
        //get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri//get image uri
                myImage.setImageURI(resultUri)
                //get drawable bitmap for text recognition
                val bitmapDrawable = myImage.getDrawable() as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val recognizer = TextRecognizer.Builder(activity!!.applicationContext).build()

                if (!recognizer.isOperational) {
                    Toast.makeText(activity, "COULDN'T READ IMAGE", Toast.LENGTH_SHORT).show()
                } else {
                    val frame = Frame.Builder().setBitmap(bitmap).build()
                    val items = recognizer.detect(frame)
                    val sb = StringBuilder()
                    //get text from sb untill there is no text
                    for (i in 0 until items.size()) {
                        val myItem = items.valueAt(i)
                        sb.append(myItem.value)
                        sb.append("\n")
                    }
                    //set Text to edit Text
                    content.setText(sb.toString())
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //if there is any error show it
                val error = result.error
                Toast.makeText(activity, "" + error, Toast.LENGTH_SHORT).show()
            }
        }
    }

/*
    TODO: Rename method, update argument and hook method into UI event
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
         * @return A new instance of fragment addNote.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addNote().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}
