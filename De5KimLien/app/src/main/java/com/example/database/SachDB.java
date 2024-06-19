package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

public class SachDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "sach.sqlite";

    public static final int DB_VERSION = 1;
    public static final String TBL_NAME = "Sach";

    public static final String COL_MASP = "Ma";
    public static final String COL_TENSP = "Ten";
    public static final String COL_GIASP = "Gia";

    public SachDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " ("
                + COL_MASP + " VARCHAR(50) PRIMARY KEY, "
                + COL_TENSP + " VARCHAR(50), "
                + COL_GIASP + " REAL )";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public Cursor getData(String sql) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(sql, null);
        } catch (Exception e) {
            return null; // Trả về null nếu có lỗi
        }
    }
    public int getNumbOfRows() {
        Cursor cursor = getData("SELECT * FROM " + TBL_NAME);
        int numbOfRows = cursor.getCount();
        cursor.close();
        return numbOfRows;
    }

    public boolean execSQL(String sql) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage().toString());
            return false;
        }
    }

    public boolean insertData(String ma ,String ten , Double gia) {
        try {
            SQLiteDatabase db = getWritableDatabase(); // Lấy database có thể ghi
            // Lệnh SQL để chèn một hàng mới
            String sql = "INSERT INTO " + TBL_NAME + " VALUES(?, ?,  ?)";

            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(0, ma);

            statement.bindString(1, ten);

            statement.bindDouble(2, gia);

            statement.executeInsert();
            return true;
        } catch (Exception e) {
            Log.e("Lỗi insertData: ", e.getMessage().toString());
            return false;
        }
    }
    public void createSampleData() {
        if (getNumbOfRows() == 0) {
            try {
                // Chèn dữ liệu mẫu vào bảng
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('M-01', 'Sách tiếng việt', 200000 )");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('M-02', 'Sách tiếng trung',  90000 )");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('M-03', 'Sách tiếng anh',  100000 )");
            } catch (Exception e) {
                Log.e("Lỗi: ", "Lỗi khi thêm dữ liệu mẫu"); // Ghi log lỗi
            }
        }
    }
}
