package com.example.sqlitekotlin

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.net.URI

class AddRecordActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    private val IMAGE_PICK_CAMERA_CODE = 102
    private val STORAGE_PICK_CODE = 103

    private var actionBar: ActionBar? = null

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    private lateinit var pImageView: ImageView
    private lateinit var addRecordButton: Button

    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        actionBar = supportActionBar
        actionBar!!.title = "Add Information"
        actionBar!!.setDisplayShowCustomEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper (this)

        cameraPermission = arrayOf(
           android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        )

        storagePermission = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        pImageView = findViewById(R.id.personImage)
        pImageView.setOnClickListener {
            imagePickDialog()
        }
        addRecordButton = findViewById(R.id.addButton)
        addRecordButton.setOnClickListener {
            inputData()
        }
    }

    private fun inputData() {

    }

    private fun imagePickDialog() {
    val options = arrayOf("camera","Gallery")
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Pick Image From")
        builder.setItems(options) { dialog, which->
            if( which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                }
                else {
                    pickFromCamera()
                }
            }
            else if ( which == 1) {
              if (!checkStoragePermission()) {
                  requestStoragePermission()

              }
                else {
                    pickFromGallery()
              }
            }
        }
        builder.create().show()
    }
    private fun pickFromGallery() {
    val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_STORAGE_CODE
        )
    }
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE)

    }
    private fun checkStoragePermission(): Boolean {
      return ContextCompat.checkSelfPermission(
          this,
          android.Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
    }
    private fun pickFromCamera(){
      val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Images Title")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Images Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }
private fun requestCameraPermission() {
    ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQUEST_CODE)
}
    private fun checkCameraPermission(): Boolean {
      val resultC = ContextCompat.checkSelfPermission(
          this,
          android.Manifest.permission.CAMERA
      ) == PackageManager.PERMISSION_GRANTED
        val resultS = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return resultC && resultS
    }
}

