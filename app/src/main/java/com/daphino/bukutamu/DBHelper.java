package com.daphino.bukutamu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DBHelper(Context context) {
        super(context, "db_tamu",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Tamu(id text null, guest_name text null, company_name text null, " +
                "meet text null,need text null,arrival text null,out text null,signature text null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Tamu");
        onCreate(db);
    }

    public boolean insertData(String id,String guest_name,String company_name,String meet,String need,String arrival, String out, String signature){
        db =this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("id",id);
        contentValues.put("guest_name",guest_name);
        contentValues.put("company_name",company_name);
        contentValues.put("meet",meet);
        contentValues.put("need",need);
        contentValues.put("arrival",arrival);
        contentValues.put("out",out);
        contentValues.put("signature",signature);
        try{
            db.insertOrThrow("Tamu",null,contentValues);
        }catch (SQLiteConstraintException ex){
            ex.printStackTrace();
            return  false;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateData(String id, String out){
        try{
            db = this.getWritableDatabase();
            ContentValues  contentValues = new ContentValues();
            contentValues.put("out",out);
            db.update("Tamu",contentValues,"id = ?",new String[] {id});
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public Cursor getAllData(){
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Tamu",null);
        return  cursor;
    }
}
