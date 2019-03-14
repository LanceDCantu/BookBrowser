package com.example.lance.bookbrowser

//import android.R
import android.content.Intent
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

class FireBaseLogin : AppCompatActivity(), View.OnClickListener {

    lateinit var mAuth: FirebaseAuth
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_login)

        mAuth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.editTextEmail) as EditText
        editTextPassword = findViewById(R.id.editTextPassword) as EditText
        progressBar = findViewById(R.id.progressbar) as ProgressBar

        findViewById<TextView>(R.id.textViewSignup).setOnClickListener(this)
        findViewById<View>(R.id.buttonLogin).setOnClickListener(this)

    }

    private fun userLogin() {
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                finish()
                val intent = Intent(this@FireBaseLogin, MainSearchActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.textViewSignup -> {
                finish()
                startActivity(Intent(this, SignUpActivity::class.java))
            }

            R.id.buttonLogin -> userLogin()
        }
    }
}