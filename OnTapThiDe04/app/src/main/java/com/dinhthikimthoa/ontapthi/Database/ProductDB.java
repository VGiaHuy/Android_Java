package com.dinhthikimthoa.ontapthi.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class ProductDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "Product.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "product";
    public static final String PD_ID ="PDId";
    public static final String PD_NAME ="PDName";
    public static final String PD_PRICE ="PDPrice";

    public ProductDB(@Nullable Context context) {
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
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'Thuốc trừ sâu',39000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'Thuốc trừ bug',40000)");
            execSql("INSERT INTO " + TABLE_NAME + " VALUES(null, 'Thuốc tạo bug',30000)");
        }
    }


}
