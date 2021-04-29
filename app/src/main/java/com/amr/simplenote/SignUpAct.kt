package com.amr.simplenote

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.log_in.progressBar
import kotlinx.android.synthetic.main.log_in.txtEmail
import kotlinx.android.synthetic.main.log_in.txtPassword
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.android.synthetic.main.signup_dialog.view.*

var error = false

class SignUpAct : AppCompatActivity(), TextWatcher {
    var fireStoreInstance: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var currentUserDocRef: DocumentReference? = null
        get() = fireStoreInstance.document("users/${mAuth.currentUser?.uid.toString()}")

    //TODO**********************ov.Rt.func******************************************************************************************************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        txtFullName.addTextChangedListener(this)
        txtEmail.addTextChangedListener(this)
        txtPassword.addTextChangedListener(this)


    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        btnSignUp.isEnabled = txtFullName.text.trim().isNotEmpty()
                && txtEmail.text.trim().isNotEmpty()
                && txtPassword.text.trim().isNotEmpty()
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}


    //TODO*******************functions***************************************************************************************************************************************************************************
    fun dialogSignUp(message: String) {

        signUpDialog = layoutInflater.inflate(R.layout.signup_dialog, null)
        dialogHolder = AlertDialog.Builder(this).setView(signUpDialog).create()
        dialogHolder.show()
        signUpDialog.dialogMess.text = message

        signUpDialog.dialogMess.setOnClickListener {
            startActivity(Intent(this, LogInAct::class.java))
            dialogHolder.dismiss()
            finish()
        }

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun sendEmailVerification() {
        val user = mAuth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener {
            if (it.isComplete) {
                startActivity(Intent(this, LogInAct::class.java))
                showToast("signed successfully ,verify email to log in")
                prograssBar(false)
                finish()
            } else {
                prograssBar(false)
                showToast(it.exception.toString())
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun createNewAccount(name: String, email: String, password: String) {
        prograssBar(true)
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                sendEmailVerification()
                currentUserDocRef?.set(name)

            } else {
                showToast(task.exception.toString())
                //dialogSignUp("current user log ln")
                prograssBar(false)
            }
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************views Actions***************************************************************************************************************************************************
    fun btnCurrentUser(v: View) {
        finish()
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun btnSignUp(v: View) {

        val name = txtFullName.text.toString()
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        checkFields(name, email, password)

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun checkFields(
        name: String,
        email: String,
        password: String
    ) {
        //Test the last field firstly ,sothat the error message appear on the upper field if there are more fields have errors
        if (password.length < 6) {
            txtPassword.error = "short,6 char at least"
            txtPassword.requestFocus()
            error = true
        }
        if (email.isEmpty()) {
            txtEmail.error = "Email Required"
            txtEmail.requestFocus()
            error = true
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.error = "please,enter a valid email"
            txtEmail.requestFocus()
            error = true
        }

        if (name.isEmpty()) {
            txtFullName.error = "Name Required"
            txtFullName.requestFocus()
            error = true

        } else {
            error = false
        }
        if (!error) {
            createNewAccount(name, email, password)
        }

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //TODO*********************simplify***************************************************************************************************************************************************************************
    fun prograssBar(b: Boolean) {
        if (b) {
            progressBar.visibility = VISIBLE
        }
        if (!b) {
            progressBar.visibility = INVISIBLE
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun showToast(toast: String) {
        Toast.makeText(applicationContext, toast, LENGTH_LONG).show()
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}