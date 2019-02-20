package com.example.developer.mynewsapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.developer.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_password.*
import java.text.SimpleDateFormat
import java.util.*

class RegistrerActivity : AppCompatActivity(), EmailFragment.Listener, PasswordFragment.Listener {

    private var globalEmail: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_registrer)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, EmailFragment()).commit()
        }
    }

    override fun onNext(email: String) {
        if (email.isNotEmpty()) {
            globalEmail = email
            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                if(it.isSuccessful){
                    if (it.result.signInMethods?.isEmpty() != false){
                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, PasswordFragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                       showToast("Этот email уже используется!")
                    }
                } else {
                    Toast.makeText(this, it.exception?.message!!, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showToast("Пожалуйста введите Email")
        }
    }

    override fun onRegister(password: String) {
        if (password.isNotEmpty()) {
            val email = globalEmail
            if (email != null) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            createUserNode()
                            startMainActivity()
                            finish()
                            showToast("Регистрация прошла успешно")
                        } else {
                            showToast("Что-то пошло не так... Пожалуйста попробуйте позже")
                        }
                    }
            } else {
               showToast("Пожалуйста введите Email")
                supportFragmentManager.popBackStack()
            }
        } else {
            showToast("Пожалуйста введите пароль")
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun createUserNode(){
        val uid = auth.currentUser?.uid
        val date = simpleDateFormat.format(Date())
        val important = false
        database.child("users/$uid/title").setValue("A")
        database.child("users/$uid/text").setValue("A")
        database.child("users/$uid/date").setValue(date)
        database.child("users/$uid/imageUrl").setValue("A")
        database.child("users/$uid/important").setValue(important)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

 class EmailFragment: Fragment() {

     private lateinit var listener: Listener

     interface Listener {
         fun onNext(email: String)
     }

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.fragment_register_email, container, false)
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         next_btn.setOnClickListener {
             val email = email_input.text.toString()
             listener.onNext(email)
         }
     }

     override fun onAttach(context: Context) {
         super.onAttach(context)
         listener = context as Listener
     }
 }

 class PasswordFragment: Fragment() {

     private lateinit var listener: Listener

     interface Listener {
        fun onRegister(password: String)
     }

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.fragment_register_password, container, false)
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         register_btn.setOnClickListener {
             val password = password_input.text.toString()
             listener.onRegister(password)
         }
     }

     override fun onAttach(context: Context) {
         super.onAttach(context)
         listener = context as Listener
     }
 }
