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
import kotlinx.android.synthetic.main.log_in.*
import kotlinx.android.synthetic.main.log_in.progressBar
import kotlinx.android.synthetic.main.log_in.txtEmail
import kotlinx.android.synthetic.main.log_in.txtPassword
import kotlinx.android.synthetic.main.signup_dialog.view.*

lateinit var mAuth: FirebaseAuth

class LogInAct : AppCompatActivity(), TextWatcher {
    //TODO**********************ov.Rt.func******************************************************************************************************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)
        txtEmail.addTextChangedListener(this)
        txtPassword.addTextChangedListener(this)

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        btnLogIn.isEnabled = txtEmail.text.trim().isNotEmpty()
                && txtPassword.text.trim().isNotEmpty()
    }

    override fun afterTextChanged(p0: Editable?) {}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onBackPressed() {
        finish()
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*******************functions***************************************************************************************************************************************************************************
    fun checkVerifyEmail() {
        val currentUser = mAuth.currentUser
        if (currentUser!!.isEmailVerified) {
            startActivity(Intent(this, MainActivity::class.java))
            showToast("logged successfully")
            finish()

        } else {
            showToast("please,verify your email")
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun letUserLogIn(email: String, password: String) {

        prograssBar(true)
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                prograssBar(false)
                checkVerifyEmail()

            } else {
                prograssBar(false)
                showToast(task.exception.toString())
            }

        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun dialogSignUp(message: String) {

        signUpDialog = layoutInflater.inflate(R.layout.signup_dialog, null)
        dialogHolder = AlertDialog.Builder(this).setView(signUpDialog).create()
        dialogHolder.show()
        signUpDialog.dialogMess.text = message
        signUpDialog.dialogMess.setOnClickListener {
            startActivity(Intent(this, SignUpAct::class.java))
            dialogHolder.dismiss()
        }

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun checkFields(email: String, password: String) {
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
        } else {
            error = false
        }
        if (!error) {
            letUserLogIn(email, password)
        }

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************views Actions***************************************************************************************************************************************************
    fun btnLogIn(v: View) {

        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        checkFields(email, password)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun btnNewUser(v: View) {
        startActivity(Intent(this, SignUpAct::class.java))
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //TODO*********************simplify***************************************************************************************************************************************************************************
    fun showToast(toast: String) {
        Toast.makeText(applicationContext, toast, LENGTH_LONG).show()
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun prograssBar(b: Boolean) {
        if (b) {
            progressBar.visibility = VISIBLE
        }
        if (!b) {
            progressBar.visibility = INVISIBLE
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}