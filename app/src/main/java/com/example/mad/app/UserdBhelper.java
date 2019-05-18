package com.example.mad.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.os.Build.ID;

/**
 * Created by MAD on 20/03/2018.
 */


public class UserdBhelper extends SQLiteOpenHelper{

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserPerformance.NewUserPerformance.table_name;

    private static final String DATABASE_NAME="userinfo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String user_id  = "id";
    public static  final String user_distance="distance";
    public static  final  String user_time = "temps";
    public static  final  String user_vitesse = "vitesse";
    public static  final  String table_name ="Performance";

    private static final String CREATE_QUERY = "CREATE TABLE "+table_name+"( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "distance REAL NOT NULL,"+
            "temps INTEGER NOT NULL,"+
            "vitesse REAL NOT NULL )";


    public UserdBhelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            Log.d("database operations","database created and opened....");
        }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
             db.execSQL(CREATE_QUERY);
             Log.d("Database OPERATIONS","Table created ...");
    }

    public void addPerfInfo(float  distance , int time, float vitesse,SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_distance,distance);
        contentValues.put(user_time,time);
        contentValues.put(user_vitesse,vitesse);
        long line=db.insert(table_name,null,contentValues);

        Log.d("line",String.valueOf(line));

    }

    public Cursor getPerformance(SQLiteDatabase db){

           String [] projections = {user_vitesse};
                Cursor cursor = db.query(table_name,projections,null,null,null,null,null);
                return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
