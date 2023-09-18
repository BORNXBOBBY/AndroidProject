package com.example.yati.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var firestore :  FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var fullName : TextView
    lateinit var points : TextView
    lateinit var profileImageText: TextView
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        profileImageText = findViewById(R.id.profile_textView)

        profileImageText.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val showBookServicePopUp = PopupMenu(
            this,
            plus_bookingImage
        )
        showBookServicePopUp.menu.add(Menu.NONE, 0, 0, "Book Service")
        showBookServicePopUp.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
         if (id==0){
             val intent = Intent(this, BookServiceActivity::class.java)
             startActivity(intent)
         }
            false
        }
        plus_bookingImage.setOnClickListener {
            showBookServicePopUp.show()

        }
        
        val navView: BottomNavigationView = findViewById(R.id.bottom_navView)

        val navController=findNavController(R.id.navigation_host)
        navView.setupWithNavController(navController)

        fullName = findViewById(R.id.user_nameText)
        points = findViewById(R.id.user_points)
    }

    override fun onResume() {
        super.onResume()
        fullName.text = AppGlobal.userFullName
        profileImageText.text = AppGlobal.userShortName
        points.text = AppGlobal.loyaltyPoint + " Points"
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menubar, menu)
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==R.id.logout_menu){
            val savePreference = this.getSharedPreferences("loginPref", Context.MODE_PRIVATE)
            savePreference.edit().clear().commit()

            AppGlobal.userFullName = ""
            AppGlobal.AppToken = ""
            AppGlobal.userid = ""

            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
        return true
    }
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
           Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()

        }
        backPressedTime = System.currentTimeMillis()
    }
}