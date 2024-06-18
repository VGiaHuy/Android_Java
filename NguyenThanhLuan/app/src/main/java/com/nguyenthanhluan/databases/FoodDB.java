package com.nguyenthanhluan.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodDB extends SQLiteOpenHelper {

    // Các hằng số cho tên database, phiên bản, tên bảng, và tên cột
    public static final String DB_NAME = "food.sqlite"; // Tên database
    public static final int DB_VERSION = 1; // Phiên bản database
    public static final String TBL_NAME = "Food"; // Tên bảng
    public static final String COL_MA = "Ma"; // Cột ID
    public static final String COL_TEN = "Ten"; // Cột tên món ăn
    public static final String COL_MOTA = "MoTa"; // Cột mô tả
    public static final String COL_GIA = "Gia"; // Cột giá
    public static final String COL_DANHMUC = "DanhMuc"; // Cột danh mục
    public static final String COL_HINHANH = "HinhAnh"; // Cột hình ảnh

    // Constructor cho lớp FoodDB, gọi constructor của lớp cha
    public FoodDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Phương thức được gọi khi database được tạo
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Lệnh SQL để tạo bảng Food nếu chưa tồn tại
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " ("
                + COL_MA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TEN + " VARCHAR(50), "
                + COL_MOTA + " VARCHAR(150), "
                + COL_GIA + " REAL, "
                + COL_DANHMUC + " VARCHAR(150), "
                + COL_HINHANH + " BLOB(150) )";

        // Thực thi lệnh SQL
        sqLiteDatabase.execSQL(sql);
    }

    // Phương thức được gọi khi database cần được nâng cấp
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Xóa bảng hiện có
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        // Tạo bảng mới
        onCreate(sqLiteDatabase);
    }

    // Phương thức để thực hiện truy vấn SELECT và trả về đối tượng Cursor
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

    // Phương thức để chèn một hàng mới vào bảng Food
    public boolean insertData(String ten, String moTa, Double gia, String danhMuc, byte[] photo) {
        try {
            SQLiteDatabase db = getWritableDatabase(); // Lấy database có thể ghi
            // Lệnh SQL để chèn một hàng mới
            String sql = "INSERT INTO " + TBL_NAME + " VALUES(null, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql); // Biên dịch lệnh SQL
            statement.clearBindings(); // Xóa các ràng buộc trước đó
            statement.bindString(1, ten); // Ràng buộc tên món ăn
            statement.bindString(2, moTa); // Ràng buộc mô tả
            statement.bindDouble(3, gia); // Ràng buộc giá
            statement.bindString(4, danhMuc); // Ràng buộc danh mục
            statement.bindBlob(5, photo); // Ràng buộc hình ảnh

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
                execSQL("INSERT INTO " + TBL_NAME + " VALUES(null, 'Thịt heo quay', 'Mô tả thịt heo quay', 20000, 'Món chính', null)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES(null, 'Tôm chiên', 'Mô tả tôm chiên', 9000, 'Món khai vị', null)");
                execSQL("INSERT INTO " + TBL_NAME + " VALUES(null, 'Chè vịt hầm', 'Mô tả chè vịt hầm', 10000, 'Món tráng miệng', null)");
            } catch (Exception e) {
                Log.e("Lỗi: ", "Lỗi khi thêm dữ liệu mẫu"); // Ghi log lỗi
            }
        }
    }
}
