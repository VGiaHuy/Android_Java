package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

public class SanPhamDB extends SQLiteOpenHelper {
    // Các hằng số cho tên database, phiên bản, tên bảng, và tên cột
    public static final String DB_NAME = "sanpham.sqlite"; // Tên database
    public static final int DB_VERSION = 1; // Phiên bản database
    public static final String TBL_NAME = "SanPham"; // Tên bảng
    public static final String COL_MA = "Ma"; // Cột ID
    public static final String COL_TEN = "Ten"; // Cột tên món ăn
    public static final String COL_GIA = "Gia"; // Cột giá

    public SanPhamDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// Lệnh SQL để tạo bảng Food nếu chưa tồn tại
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " ("
                + COL_MA + " VARCHAR(50) PRIMARY KEY, "
                + COL_TEN + " VARCHAR(100), "
                + COL_GIA + " REAL )";

        // Thực thi lệnh SQL
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng hiện có
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        // Tạo bảng mới
        onCreate(db);
    }

    public Cursor getData(String sql) {
        try {
            SQLiteDatabase db = getReadableDatabase(); // Lấy database chỉ đọc
            return db.rawQuery(sql, null); // Thực hiện truy vấn và trả về kết quả
        } catch (Exception e) {
            return null; // Trả về null nếu có lỗi
        }
    }

    // Phương thức để lấy số lượng hàng trong bảng Food
    public int getNumbOfRows() {
        Cursor cursor = getData("SELECT * FROM " + TBL_NAME); // Thực hiện truy vấn SELECT
        int numbOfRows = cursor.getCount(); // Lấy số lượng hàng
        cursor.close(); // Đóng cursor
        return numbOfRows; // Trả về số lượng hàng
    }

    // Phương thức để thực hiện truy vấn INSERT, UPDATE, hoặc DELETE
    public boolean execSQL(String sql) {
        try {
            SQLiteDatabase db = getWritableDatabase(); // Lấy database có thể ghi
            db.execSQL(sql); // Thực thi lệnh SQL
            return true; // Trả về true nếu thành công
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage().toString()); // Ghi log lỗi
            return false; // Trả về false nếu có lỗi
        }
    }

    public boolean insertData(String ma, String ten, Double gia) {
        try {
            SQLiteDatabase db = getWritableDatabase(); // Lấy database có thể ghi
            // Lệnh SQL để chèn một hàng mới
            String sql = "INSERT INTO " + TBL_NAME + " VALUES(null, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql); // Biên dịch lệnh SQL
            statement.clearBindings(); // Xóa các ràng buộc trước đó
            statement.bindString(0, ma); // Ràng buộc tên món ăn
            statement.bindString(1, ten); // Ràng buộc tên món ăn
            statement.bindDouble(2, gia); // Ràng buộc giá


            statement.executeInsert(); // Thực thi lệnh chèn
            return true; // Trả về true nếu thành công
        } catch (Exception e) {
            Log.e("Lỗi insertData: ", e.getMessage().toString()); // Ghi log lỗi
            return false; // Trả về false nếu có lỗi
        }
    }

    // Phương thức để tạo dữ liệu mẫu trong bảng Food
    public void createSampleData() {
        if (getNumbOfRows() == 0) { // Kiểm tra nếu bảng trống
            try {
                // Chèn dữ liệu mẫu vào bảng
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('SP-111', 'Vertu  Constellation',  20000)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('SP-123', 'IPhone 5S',  10000)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('SP-134', 'Nokia Lumia 925',  50000)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('SP-145', 'SamSung GalaxyS4',  45000)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('SP-156', 'HTC Desire600',  70000)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES('SP-167', 'HKPhone Revo LEAD',  56000)");

            } catch (Exception e) {
                Log.e("Lỗi: ", "Lỗi khi thêm dữ liệu mẫu"); // Ghi log lỗi
            }
        }
    }
}
