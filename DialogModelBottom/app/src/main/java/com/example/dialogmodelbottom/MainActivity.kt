package com.example.dialogmodelbottom

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        findViewById<Button>(R.id.buttonSheet).setOnClickListener {
            val dialog= BottomSheetDialog(this)
            val view=layoutInflater.inflate(R.layout.buttonsheetdesign,null)
            val camera=view.findViewById<ImageView>(R.id.click_image)
            val gallery =view.findViewById<ImageView>(R.id.gallery_image)
            gallery.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
            camera.setOnClickListener{
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, pic_id)
            }

            view.findViewById<ImageView>(R.id.closeButton).setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
        if (requestCode== pic_id){
            val photo=data!!.extras!!["data"] as Bitmap
            imageView.setImageBitmap(photo)
        }
    }
    companion object {
        // Define the pic id
        private const val pic_id = 123
    }

}