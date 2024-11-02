package com.example.studentregistrationform

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context):

    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){


    companion object{
        private const val DATABASE_NAME = "StudentDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "studentData"

        private const val SerialNo = "SrNo"
        private const val First_Name = "FirstName"
        private const val Middle_Name = "MiddleName"
        private const val Last_Name = "LastName"
        private const val DO_B = "DOB"
        private const val GenderSelect = "Gender"
        private const val Aadhar_No = "AadharNo"
    }


    override fun onCreate(db: SQLiteDatabase?){

        val createTableQuery =
            ( "CREATE TABLE $TABLE_NAME (" + "$SerialNo INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$First_Name Text," + "$Middle_Name Text," + "$Last_Name Text,"
                    + "$DO_B Text," + "$GenderSelect Text," + "$Aadhar_No Text)" )
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertStudentData(
        FirstName: String,
        MiddleName: String,
        LastName: String,
        DOB: String,
        Gender: String,
        AadharNo: String
    ): Long {
        val value = ContentValues().apply {
            put(First_Name, FirstName)
            put(Middle_Name, MiddleName)
            put(Last_Name, LastName)
            put(DO_B, DOB)
            put(GenderSelect, Gender)
            put(Aadhar_No, AadharNo)
        }

        val db = writableDatabase
        return db.insert(TABLE_NAME, null, value)
    }
}