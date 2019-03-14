package com.example.lance.bookbrowser

//import android.R
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var progressBar: ProgressBar
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextEmail = findViewById(R.id.editTextEmail) as EditText
        editTextPassword = findViewById(R.id.editTextPassword) as EditText
        progressBar = findViewById(R.id.progressbar) as ProgressBar

        mAuth = FirebaseAuth.getInstance()

        findViewById<View>(R.id.buttonSignUp).setOnClickListener(this)
        findViewById<TextView>(R.id.textViewLogin).setOnClickListener(this)
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString().trim { it <= ' ' }
        val password = editTextPassword.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            editTextEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Please enter a valid email"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword.error = "Minimum length of password should be 6"
            editTextPassword.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE

        this.mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                //Registration OK
                var intent = Intent(this, MainSearchActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
                //val firebaseUser = this.mAuth!!.currentUser!!
            } else {
                //Registration error
                Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_SHORT).show()
            }
        }

//        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
//            if(task.isSuccessful){
//                var intent = Intent(this, AboutUs::class.java)
//                startActivity(intent)
//                Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
//            }else{
//                Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_SHORT).show()
//            }
//
//        })

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonSignUp -> registerUser()

            R.id.textViewLogin -> {
                finish()
                startActivity(Intent(this, FireBaseLogin::class.java))
            }
        }
    }
}