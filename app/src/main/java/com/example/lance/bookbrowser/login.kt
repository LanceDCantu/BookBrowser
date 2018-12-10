package com.example.lance.bookbrowser

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            var status=if(username_et.text.toString().equals("firstui")
                && password_et.text.toString().equals("password")) "Logged In Successfully" else "Login failed"
            Toast.makeText(this,status, Toast.LENGTH_SHORT).show()
        }
    }
}
