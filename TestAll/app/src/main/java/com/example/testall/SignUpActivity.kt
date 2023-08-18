package com.example.testall


import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SignUpActivity : AppCompatActivity() {
    val db= FirebaseFirestore.getInstance()
    var storageRef= Firebase.storage.reference
    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val mAuth=FirebaseAuth.getInstance()

        val db= FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance().currentUser?.uid

        val gallaryAccess = findViewById<ImageView>(R.id.images)
        val names = findViewById<TextView>(R.id.names)
        val emails = findViewById<TextView>(R.id.emails)
        val logoutBtn=findViewById<Button>(R.id.logout)
        val EditButton = findViewById<Button>(R.id.edit)

        val userName = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val submit = findViewById<Button>(R.id.submit)
        val login = findViewById<Button>(R.id.login)



        EditButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))

        }
        if (auth != null) {
            db.collection("users").document(auth)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) { // Check if snapshot is not null and exists

                        val myData: DataModel? = snapshot.toObject(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null

//                            FashionList.add(myData)

                            names.setText(myData.name)
//                            number.setText(myData.number)
                            emails.setText(myData.email)
                            Glide.with(this)
                                .load(myData.image)
                                .error(R.drawable.ic_launcher_background)
                                .into(gallaryAccess)


                        } else {
                            Log.e(ContentValues.TAG, "DataModel is null")
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Snapshot is null or doesn't exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error adding document", e)
                }
        }

        logoutBtn.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this,LoginPageActivity::class.java) )
            Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()
        }

//        Restriction password
        val allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#\$%^&*()_-+=[]{}|;:',.<>/?`~"
        password.filters = arrayOf(CharacterInputFilter(allowedCharacters))
        password.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
        val maxLength = 8
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        password.filters = filterArray

        submit.setOnClickListener {



            val text = password.text.toString()
            val hasLowerCase = text.matches(Regex(".*[a-z].*"))
            val hasUpperCase = text.matches(Regex(".*[A-Z].*"))
            val hasDigit = text.matches(Regex(".*\\d.*"))
            val hasSpecialCharacter = text.matches(Regex(".*[!@#\$%^&*()\\-_+=\\[\\]{}|;:',.<>/?`~].*"))
            if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialCharacter) {
                Toast.makeText(this, "Password is valid", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,DashBoardActivity::class.java))
            } else {
                Toast.makeText(this, "Password is invalid", Toast.LENGTH_SHORT).show()
            }

//            val user = hashMapOf(
//                "username" to userName.text.toString(),
//                "password" to password.text.toString(),
//            )
//
//
//            db.collection("usersdata").add(user)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
////                    startActivity(Intent(this,FetchDataActivity::class.java))
////                    name.text.clear()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
//                }


        }

//        val list=findViewById<ListView>(R.id.list_item)
//
//
//        db.collection("usersdata").get().addOnSuccessListener {
//            val dataModels = it.toObjects(DataModel::class.java)
//            list.adapter=ListViewAdapter(this,dataModels)
//
//        }.addOnFailureListener {
//
//
//        }

        login.setOnClickListener {

        }
    }
}