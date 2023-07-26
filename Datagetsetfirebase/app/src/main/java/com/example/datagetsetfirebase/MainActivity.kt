package com.example.datagetsetfirebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    private  lateinit var imageView: ImageView
    private lateinit var selectImgBtn: Button
    private lateinit var uploadImgBtn: Button
    private var storageRef=Firebase.storage
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db= FirebaseFirestore.getInstance()

        val name=findViewById<EditText>(R.id.name)
        val title=findViewById<EditText>(R.id.title)
        val born=findViewById<EditText>(R.id.born)
        val button=findViewById<Button>(R.id.button)
        val selectImgBtn = findViewById<Button>(R.id.select_img)
        val uploadImgBtn = findViewById<Button>(R.id.upload_img)
        val imageView = findViewById<ImageView>(R.id.imageView)


        val storageRef = Firebase.storage.reference;

        val galleryImage=registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                if (it != null) {
                    uri =it
                }
            }
        )

        selectImgBtn.setOnClickListener {
            galleryImage.launch("image/*")
        }


        button.setOnClickListener {
            val user = hashMapOf(
                "first" to name.text.toString(),
                "last" to title.text.toString(),
                "born" to born.text.toString()
            )


            db.collection("users").add(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this,FetchDataActivity::class.java))
                    name.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}