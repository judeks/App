package com.example.mad.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by MAD on 24/03/2018.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {

    public String  compare(float[] tableau){
       /* int valuePlast =  ((tableau.length) - 1);
        int valuePbeflast = ((tableau.length) - 2);*/
        float valueperformancelast=  tableau[0];
        float valueperformancebef = tableau[1];
        String test="";
        //if the last performance is less than the previous performance we show a Message otherwise we an other message
       // for(int i=0;i < tableau.length;i++){
            if(valueperformancelast>valueperformancebef){
                test = "Votre performance a augmenté. continuez Du courage";
            }
            else {
                test = "Votre performance a diminué aujourd'hui.Travaillez encore plus!";
            }
        //}
        return test ;
    }

     Context context;
    BackgroundTask(Context context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String method=params[0];

          //insert les données en arriere plan
        UserdBhelper  userdBhelper = new UserdBhelper(context);
         if(method.equals("insert")){

             float distance = Float.parseFloat(params[1]);
             int seconds = Integer.parseInt(params[2]);
             float  vitesse = Float.parseFloat(params[3]);

             SQLiteDatabase sqLiteDatabase = userdBhelper.getWritableDatabase();
             userdBhelper.addPerfInfo(distance,seconds,vitesse,sqLiteDatabase);
         }

         String varget = params[4];
         //get the performance from database
        if(varget.equals("get")) {
             SQLiteDatabase db = userdBhelper.getReadableDatabase();
             Cursor cursor = userdBhelper.getPerformance(db);
             float[] array = new float[cursor.getCount()];
             int i = 0;
            cursor.moveToLast();
            int x=cursor.getCount();
            array[0] = cursor.getFloat(cursor.getColumnIndex("vitesse"));
            cursor.moveToPosition(x-1);
            array[1] = cursor.getFloat(cursor.getColumnIndex("vitesse"));

            /* while (cursor.moveToNext()) {
                 float data = cursor.getFloat(cursor.getColumnIndex("vitesse"));
                 array[i] = data;
                 i++;
             }*/
             return compare(array);
         }
        return null;
    }





    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String resultat) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.mainActivity);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(resultat);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog=alertDialog.create();
        dialog.show();
    }
}
