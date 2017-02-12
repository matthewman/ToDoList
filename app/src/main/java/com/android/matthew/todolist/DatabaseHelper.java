package com.android.matthew.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Matthew on 11/2/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DBName = "Data";
    private static final int DBVer = 1;
    public static final String DBTable = "Task";
    public static final String DBColumn = "TaskName";


    public DatabaseHelper(Context context) {
        super(context, DBName, null, DBVer);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);",DBTable,DBColumn);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = String.format("DELETE TABLE IF EXIST %s",DBTable);
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public void insertNewTask(String task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBColumn,task);
        sqLiteDatabase.insertWithOnConflict(DBTable,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBTable,DBColumn + " = ?",new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList(){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBTable, new String[]{DBColumn},null,null,null,null,null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DBColumn);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        sqLiteDatabase.close();
        return taskList;
    }
}
