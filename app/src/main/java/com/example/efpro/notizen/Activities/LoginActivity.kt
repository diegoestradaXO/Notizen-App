package com.example.efpro.notizen.Activities

/*This is an activity from Firebase al copyrights reserved*/
/*We took these activity from Firebase and make some changes*/
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_emailpassword.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailpassword)

        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        verifyEmailButton.setOnClickListener(this)
        val animation = AnimationUtils.loadAnimation(this,R.anim.rotate)
        icon.startAnimation(animation)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser!=null){
            val intento = Intent(this, navigate::class.java)//Redirigimos a contactos
            when {
                intent?.action == Intent.ACTION_SEND -> {
                    if ("text/plain" == intent.type) {
                        val message = intent.getStringExtra(Intent.EXTRA_TEXT)
                        intento.putExtra("content",message)
                    }
                }
            }
            startActivity(intento)
            this.finish()
        }
        updateUI(currentUser)
    }
    // [END on_start_check_user]

        //Function to create account, requires an email and password
    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }

        //showProgressDialog()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(this,"DON´T FORGET TO SEND VERIFICATION EMAIL",Toast.LENGTH_LONG).show()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed, probably this email is already in use",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                //hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }

    //Function to Sign In, in order to get into the app, the user must fill the spaces
    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        //showProgressDialog()

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    val intento = Intent(this, navigate::class.java)//Redirigimos a contactos
                    startActivity(intento)
                    this.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                }
                //hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

    //After the user filled the spaces with the info required, a verification msg is sent to the user´s mail
    //The user must click the verification URL in order to sign In

    private fun sendEmailVerification() {
        // Disable button
        verifyEmailButton.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    //This function is used in the Sign In fun. Verify if the user has filled the text fields, if
    //not, the fun returns false, and the sign in can not be completed
    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        //hideProgressDialog()
        if (user != null) {
            status.text = getString(R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            emailPasswordButtons.visibility = View.GONE
            emailPasswordFields.visibility = View.GONE
            signedInButtons.visibility = View.VISIBLE
            verifyEmailButton.isEnabled = !user.isEmailVerified

        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            emailPasswordButtons.visibility = View.VISIBLE
            emailPasswordFields.visibility = View.VISIBLE
            signedInButtons.visibility = View.GONE
        }
    }

    //Adds the new Acount to the global list in the DB
    private fun writeNewUser(){

            var mDatabase = FirebaseDatabase.getInstance().reference
            val user = auth.currentUser
            val seguidores = listOf<String>(user!!.uid)
            val seguidos= listOf<String>(user.uid)
            val newuser= User(user.uid, user.email!!,"NoBiography","UnNamed",seguidores,seguidos)
            mDatabase.child("users").child(user.uid).setValue(newuser)

    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.signOutButton -> signOut()
            R.id.verifyEmailButton -> {
                sendEmailVerification()
                writeNewUser()
            }

        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}