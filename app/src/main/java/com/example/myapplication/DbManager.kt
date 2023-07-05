package com.example.myapplication
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {

    val dbName = "MyNotes"
    val dbNotesTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion=1
    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+ dbNotesTable +" ("+ colID +" INTEGER PRIMARY KEY,"+
                colTitle + " TEXT, "+ colDes +" TEXT);"


    var sqlDB:SQLiteDatabase?=null
    constructor(context: Context){
        var db=DatabaseHelperNotes(context)
        sqlDB=db.writableDatabase
    }
    inner class DatabaseHelperNotes:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context: Context):super(context,dbName,null,dbVersion) {
            this.context=context
        }
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            //Toast.makeText(context," Database is created", Toast.LENGTH_LONG).show()
        }
         override fun onUpgrade(p0: SQLiteDatabase?, p1:Int, p2: Int) {

            p0!!.execSQL ("Drop table IF EXISTS "+dbNotesTable)



         }}
   fun insertNote(values: ContentValues) :Long{
        val id=sqlDB!!.insert(dbNotesTable,"",values)
        return id
    }
    fun query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor{
        val db=SQLiteQueryBuilder()
        db.tables= dbNotesTable
        val cursor=db.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }
    fun delete(selection: String,selectionArgs: Array<String>):Int{
        val count=sqlDB!!.delete(dbNotesTable,selection,selectionArgs)
        return count
    }
    fun update(values: ContentValues,selection: String,selectionArgs: Array<String>):Int{
        val count=sqlDB!!.update(dbNotesTable,values,selection,selectionArgs)
        return count
    }



}