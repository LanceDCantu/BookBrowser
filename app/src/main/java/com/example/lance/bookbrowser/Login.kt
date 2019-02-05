package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var status: String;

        login_btn.setOnClickListener {
            if(username_et.text.toString().equals("firstui")
                && password_et.text.toString().equals("password"))
            {
               status = "Logged In Successfully"

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                status = "Login failed"
            }
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
    }
}