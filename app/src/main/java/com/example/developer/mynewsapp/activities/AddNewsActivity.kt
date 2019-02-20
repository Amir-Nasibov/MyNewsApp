package com.example.developer.mynewsapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.MenuItem
import com.example.developer.mynewsapp.GlideApp
import com.example.developer.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_news.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddNewsActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private val TAKE_PICTURE_REQUEST_CODE = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: StorageReference
    private lateinit var database: DatabaseReference
    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_add_news)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance().reference
        database = FirebaseDatabase.getInstance().reference

        add_news_image.setOnClickListener{
            takeCameraPicture()
        }
        add_news_button.setOnClickListener{
            uploadData()
        }
    }

    private fun takeCameraPicture() {
       val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val imageFile = createImageFile()
            imageUri = FileProvider.getUriForFile(
                this, "com.example.developer.mynewsapp.fileprovider", imageFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE)
        }
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return  File.createTempFile("JPEG_${simpleDateFormat.format(Date())}_", "jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            GlideApp.with(this).load(imageUri).centerCrop().into(add_news_image)
        }
    }

    private fun uploadData() {
        val title = add_news_title.text.toString()
        val text = add_news_text.text.toString()
        val date = simpleDateFormat.format(Date())
        val important = false
        val uid = auth.currentUser?.uid
        if (title.isNotEmpty() && text.isNotEmpty()) {
            storage.child("users/$uid/photo").putFile(imageUri).addOnCompleteListener {
                if (it.isSuccessful) {
                    database.child("users/$uid/title").setValue(title)
                    database.child("users/$uid/text").setValue(text)
                    database.child("users/$uid/date").setValue(date)
                    database.child("users/$uid/imageUrl").setValue(it.result.downloadUrl.toString())
                    database.child("users/$uid/important").setValue(important)
                    startActivity(Intent(this, MainActivity::class.java))
                    showToast("Пост отправлен")
                } else {
                    showToast(it.exception?.message!!)
                }
            }
        } else {
            showToast("Заполнены не все необходимые данные!")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
