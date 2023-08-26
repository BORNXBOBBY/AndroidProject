package com.example.sqlitecrudandroid

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    // creating variables for our edittext, button and dbhandler
    private var courseNameEdt: EditText? = null
    private var courseTracksEdt: EditText? = null
    private var courseDurationEdt: EditText? = null
    private var courseDescriptionEdt: EditText? = null
    private var addCourseBtn: Button? = null
    private var dbHandler: DBHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing all our variables.
        courseNameEdt = findViewById(R.id.idEdtCourseName)
        courseTracksEdt = findViewById(R.id.idEdtCourseTracks)
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration)
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription)
        addCourseBtn = findViewById(R.id.idBtnAddCourse)

        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = DBHandler(this@MainActivity)

        // below line is to add on click listener for our add course button.
        addCourseBtn.setOnClickListener(View.OnClickListener { // below line is to get data from all edit text fields.
            val courseName = courseNameEdt.getText().toString()
            val courseTracks = courseTracksEdt.getText().toString()
            val courseDuration = courseDurationEdt.getText().toString()
            val courseDescription = courseDescriptionEdt.getText().toString()

            // validating if the text fields are empty or not.
            if (courseName.isEmpty() && courseTracks.isEmpty() && courseDuration.isEmpty() && courseDescription.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter all the data..", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }

            // on below line we are calling a method to add new
            // course to sqlite data and pass all our values to it.
            dbHandler!!.addNewCourse(courseName, courseDuration, courseDescription, courseTracks)

            // after adding the data we are displaying a toast message.
            Toast.makeText(this@MainActivity, "Course has been added.", Toast.LENGTH_SHORT).show()
            courseNameEdt.setText("")
            courseDurationEdt.setText("")
            courseTracksEdt.setText("")
            courseDescriptionEdt.setText("")
        })
    }
}
