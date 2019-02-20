package com.example.developer.mynewsapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.developer.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authorization.*

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, text, duration).show()
}

class AuthorizationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_authorization)

        auth_enter.setOnClickListener(this)
        auth_register.setOnClickListener(this)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.auth_enter -> {
                val email = auth_email.text.toString()
                val password = auth_password.text.toString()
                if (validate(email, password)) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful){
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            showToast("Авторизация прошла успешно")
                        } else{
                            showToast("Email и пароль введены неверно")
                        }
                    }
                } else{
                    showToast("Пожалуйста введите email и пароль")
                }
            }
            R.id.auth_register -> {
                startActivity(Intent(this, RegistrerActivity::class.java))
            }
        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
