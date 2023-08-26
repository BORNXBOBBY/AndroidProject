package com.example.sqlitecrudandroid

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_COl + " TEXT," +
                EMAIL_COL + " TEXT," +
                USER_COL + " TEXT," +
                PASS_COL + " TEXT," +
                CONTACT_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(name: String, email: String, password: String, userId: String, contact:String):Boolean{

        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(EMAIL_COL, email)
        values.put(USER_COL, userId)
        values.put(PASS_COL, password)
        values.put(CONTACT_COL,contact)

        val db = this.writableDatabase
        val insertDataValue=  db.insert(TABLE_NAME, null, values)
        db.close()
        return !insertDataValue.equals(-1)

    }

    fun viewData(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    fun updateData(id:String, name: String, email: String, password: String, userId: String,contact: String):Boolean
    {
        val db = this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(NAME_COl,name)
        contentValues.put(EMAIL_COL,email)
        contentValues.put(PASS_COL,password)
        val upDateDataValue=db.update(TABLE_NAME,contentValues,"$ID_COL=?",arrayOf(id))
        return !upDateDataValue.equals(-1)
    }

    fun deleteData(id: String): Boolean {
        val db = this.writableDatabase

        val deleteDataValue=db.delete(TABLE_NAME,"$ID_COL=?", arrayOf(id))
        return !deleteDataValue.equals(-1)
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "ClassTest"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "College"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val NAME_COl = "name"

        // below is the variable for age column
        val EMAIL_COL = "email"

        val USER_COL = "userId"

        val PASS_COL = "password"

        val CONTACT_COL = "contact"
    }
}