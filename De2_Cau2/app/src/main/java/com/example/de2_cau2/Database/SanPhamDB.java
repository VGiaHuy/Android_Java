package com.example.de2_cau2.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SanPhamDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "SanPham.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "SanPham";
    public static final String PD_ID ="SPId";
    public static final String PD_NAME ="SPName";
    public static final String PD_PRICE ="SPPrice";

    public SanPhamDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +"("+
                PD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PD_NAME + " NVARCHAR(100), "+
                PD_PRICE + " REAL) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData (String sql){
        Cursor cursor = null;
        try{
            SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(sql,null);
        }catch (Exception ex){
            return cursor;
        }

    }

    public boolean execSql (String sql){
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(sql);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    // lấy số hàng
    public int getNumbOfRow (){
        Cursor cursor = getData(" SELECT * FROM " + TABLE_NAME);
        int numbOfRow = cursor.getCount();
        cursor.close();
        return numbOfRow;
    }

    public boolean insertData(String name, double price){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "INSERT INTO "+ TABLE_NAME + " VALUES (null, ?,? ) ";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, name);
            statement.bindDouble(2, price);
            statement.executeInsert();
            return true;
        }catch (Exception ex){
            return false;
        }

    }

    public void CreateSampleData (){
        if (getNumbOfRow() == 0 ){
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'Vertu',3999000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'IPhone 5S',4000000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'NOKIA LUMIA',3000000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'SamSung Galaxy S4',3099000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'HTC One X',3900000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'HK PHONE',2000000)");

        }
    }

}
