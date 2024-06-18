package com.nguyenthanhluan.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nguyenthanhluan.adapters.FoodAdapter;
import com.nguyenthanhluan.databases.FoodDB;
import com.nguyenthanhluan.models.Food;
import com.nguyenthanhluan.test.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;    // Khai báo biến binding để sử dụng data binding
    FoodDB foodDB;  // Đối tượng quản lý cơ sở dữ liệu
    FoodAdapter foodAdapter;    // Adapter để hiển thị danh sách món ăn
    ArrayList<Food> foodArrayList;  // Danh sách món ăn
    private byte[] photo = new byte[0];     // Mảng byte lưu trữ ảnh (chưa sử dụng trong mã này)
    private ImageView imgPreview;   // ImageView để hiển thị hình ảnh (chưa sử dụng trong mã này)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.nguyenthanhluan.test.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Sử dụng data binding để set layout cho Activity

        prepareDb(); // Chuẩn bị cơ sở dữ liệu và tạo dữ liệu mẫu
        loadData(); // Load dữ liệu từ cơ sở dữ liệu lên ListView
        addEvents(); // Xử lý sự kiện khi người dùng tương tác trên giao diện

        // Khởi tạo và thiết lập Spinner danh mục tìm kiếm
        String[] danhMucArray = {"Tất cả", "Khai vị", "Món chính", "Tráng miệng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhMucArray);
        binding.spnDanhMucSearch.setAdapter(adapter);
    }


    private void prepareDb() {
        foodDB = new FoodDB(this); // Khởi tạo đối tượng quản lý cơ sở dữ liệu
        foodDB.createSampleData(); // Tạo dữ liệu mẫu trong cơ sở dữ liệu nếu chưa có
    }


    private void loadData() {
        foodAdapter = new FoodAdapter(MainActivity.this, R.layout.itemfood, getDataFromDb());
        binding.lvFood.setAdapter(foodAdapter); // Gắn adapter vào ListView để hiển thị danh sách món ăn
    }


    private List<Food> getDataFromDb() {
        foodArrayList = new ArrayList<>(); // Khởi tạo danh sách món ăn
        Cursor cursor = foodDB.getData("SELECT * FROM " + FoodDB.TBL_NAME); // Truy vấn và lấy dữ liệu từ cơ sở dữ liệu

        while (cursor.moveToNext()) {
            // Đọc thông tin từ Cursor và thêm vào danh sách món ăn
            foodArrayList.add(new
                    Food(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getBlob(5)));
        }

        cursor.close(); // Đóng Cursor sau khi đã sử dụng xong
        return foodArrayList; // Trả về danh sách món ăn
    }


    private void addEvents() {
        // Xử lý sự kiện khi người dùng click vào nút Tìm kiếm
        binding.btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin từ EditText và Spinner để tìm kiếm
                String edtTimKiemTen = binding.edtTimKiemTen.getText().toString().trim();
                String edtGiaTuStr = binding.edtGiaTu.getText().toString().trim();
                String edtGiaDenStr = binding.edtGiaDen.getText().toString().trim();
                String danhMucTimKiem = binding.spnDanhMucSearch.getSelectedItem().toString().trim();

                double edtGiaTu = -1;
                double edtGiaDen = -1;

                try {
                    if (!edtGiaTuStr.isEmpty()) {
                        edtGiaTu = Double.parseDouble(edtGiaTuStr); // Ép kiểu và lấy giá trị giá từ
                    }
                    if (!edtGiaDenStr.isEmpty()) {
                        edtGiaDen = Double.parseDouble(edtGiaDenStr); // Ép kiểu và lấy giá trị giá đến
                    }
                } catch (NumberFormatException e) {
                    // Xử lý ngoại lệ khi người dùng nhập giá trị không hợp lệ
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập giá trị hợp lệ cho giá", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thực hiện tìm kiếm và xử lý kết quả
                Cursor cursor = searchData(edtTimKiemTen, edtGiaTu, edtGiaDen, danhMucTimKiem);

                if (cursor != null) {
                    try {
                        if (cursor.getCount() > 0) {
                            foodArrayList.clear(); // Xóa dữ liệu cũ trong danh sách món ăn

                            while (cursor.moveToNext()) {
                                // Đọc dữ liệu từ Cursor và thêm vào danh sách món ăn
                                foodArrayList.add(new
                                        Food(cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getDouble(3),
                                        cursor.getString(4),
                                        cursor.getBlob(5)));
                            }

                            cursor.close(); // Đóng Cursor sau khi đã sử dụng xong
                            foodDB.close(); // Đóng cơ sở dữ liệu sau khi đã sử dụng xong
                            foodAdapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị kết quả tìm kiếm
                        } else {
                            foodDB.close(); // Đóng cơ sở dữ liệu khi không tìm thấy kết quả
                            foodArrayList.clear(); // Xóa dữ liệu cũ trong danh sách món ăn
                            foodAdapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị thông báo không tìm thấy kết quả

                            Toast.makeText(getApplicationContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                        }
                    } finally {
                        cursor.close(); // Đóng Cursor sau khi đã sử dụng xong
                        foodDB.close(); // Đóng cơ sở dữ liệu sau khi đã sử dụng xong
                    }
                }
            }
        });
    }


    // Sự kiện khi nhấn button sửa thông tin món ăn
    public void openEditDialog(Food p) {
        Dialog dialog = new Dialog(this); // Tạo đối tượng Dialog mới
        dialog.setContentView(R.layout.editfood); // Thiết lập layout cho Dialog từ file editfood.xml

        // Lấy các EditText để sửa thông tin món ăn
        EditText edtTen = dialog.findViewById(R.id.edtTenEdit);
        edtTen.setText(p.getTenMonAn());

        EditText edtMoTa = dialog.findViewById(R.id.edtMoTaEdit);
        edtMoTa.setText(p.getMoTaMonAn());

        EditText edtGia = dialog.findViewById(R.id.edtGiaEdit);
        edtGia.setText(String.valueOf(p.getGiaMonAn()));

        // Thêm Spinner danh mục để chọn loại món ăn
        Spinner spnDanhMuc = dialog.findViewById(R.id.spnDanhMucEdit);
        String[] danhMucArray = {"Khai vị", "Món chính", "Tráng miệng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhMucArray);
        spnDanhMuc.setAdapter(adapter);
        // Chọn danh mục hiện tại của món ăn
        for (int i = 0; i < danhMucArray.length; i++) {
            if (danhMucArray[i].equals(p.getDanhMucMonAn())) {
                spnDanhMuc.setSelection(i);
                break;
            }
        }

        // Hiển thị hình ảnh xem trước nếu có
        imgPreview = dialog.findViewById(R.id.imvHinhAnhEdit);
        if (p.getHinhAnhMonAn() != null) {
            byte[] imageByteArray = p.getHinhAnhMonAn();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imgPreview.setImageBitmap(bitmap);
            photo = imageByteArray;
        }

        // Xử lý khi người dùng nhấn nút Thêm hình ảnh
        Button btnThemHinhAnh = dialog.findViewById(R.id.btnThemHinhAnhEdit);
        btnThemHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1); // Mở activity để chọn hình ảnh từ bộ nhớ
            }
        });

        // Xử lý khi người dùng nhấn nút Lưu để cập nhật thông tin món ăn
        Button btnSave = dialog.findViewById(R.id.btnSuaMonAn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString();
                String moTa = edtMoTa.getText().toString();
                double gia = Double.parseDouble(edtGia.getText().toString());
                String danhMuc = spnDanhMuc.getSelectedItem().toString();

                // Cập nhật dữ liệu món ăn vào cơ sở dữ liệu
                boolean updated = updateData(p.getMaMonAn(), ten, moTa, gia, danhMuc, photo);

                if (updated) {
                    loadData(); // Load lại dữ liệu sau khi cập nhật thành công
                    dialog.dismiss(); // Đóng Dialog sau khi cập nhật thành công
                } else {
                    Toast.makeText(MainActivity.this, "Không thể cập nhật!. Hãy thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thiết lập kích thước và hiển thị Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    // Xác nhận xóa món ăn
    public void openDeleteConfirmDialog(Food p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Xác nhận xóa"); // Thiết lập tiêu đề cho Dialog xác nhận xóa
        builder.setIcon(android.R.drawable.ic_delete); // Thiết lập biểu tượng cho Dialog
// Thiết lập nội dung thông báo xác nhận xóa món ăn
        builder.setMessage("Bạn có chắc muốn xóa sản phẩm: " + p.getTenMonAn() + "?");

        // Xử lý khi người dùng nhấn nút Ok để xác nhận xóa
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                foodDB.execSQL("DELETE FROM " + foodDB.TBL_NAME + " WHERE " + foodDB.COL_MA + " = " + p.getMaMonAn());
                loadData(); // Load lại dữ liệu sau khi xóa thành công
                dialogInterface.dismiss(); // Đóng Dialog xác nhận sau khi xóa
            }
        });

        // Xử lý khi người dùng nhấn nút Cancel để hủy bỏ việc xóa
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // Đóng Dialog xác nhận khi người dùng hủy bỏ
            }
        });

        // Tạo và hiển thị Dialog xác nhận xóa
        Dialog dialog = builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    // Cập nhật thông tin món ăn trong cơ sở dữ liệu
    public boolean updateData(int maMonAn, String ten, String moTa, double gia, String danhMuc, byte[] hinhAnh) {
        SQLiteDatabase db = this.foodDB.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi dữ liệu
        ContentValues contentValues = new ContentValues(); // Đối tượng để lưu các giá trị cần cập nhật
        contentValues.put(foodDB.COL_TEN, ten); // Đặt giá trị cho cột Tên món ăn
        contentValues.put(foodDB.COL_MOTA, moTa); // Đặt giá trị cho cột Mô tả món ăn
        contentValues.put(foodDB.COL_GIA, gia); // Đặt giá trị cho cột Giá món ăn
        contentValues.put(foodDB.COL_DANHMUC, danhMuc); // Đặt giá trị cho cột Danh mục món ăn
        contentValues.put(foodDB.COL_HINHANH, hinhAnh); // Đặt giá trị cho cột Hình ảnh món ăn
        db.update(foodDB.TBL_NAME, contentValues, foodDB.COL_MA + " = ?", new String[]{String.valueOf(maMonAn)});
        // Thực hiện câu lệnh update trong bảng foodDB.TBL_NAME với điều kiện là foodDB.COL_MA = maMonAn
        return true; // Trả về true nếu cập nhật thành công
    }

    // Tìm kiếm món ăn trong cơ sở dữ liệu
    public Cursor searchData(String ten, double giaTu, double giaDen, String danhMuc) {
        SQLiteDatabase db = this.foodDB.getWritableDatabase(); // Mở cơ sở dữ liệu để đọc dữ liệu

        String query = "SELECT * FROM " + foodDB.TBL_NAME + " WHERE 1=1"; // Câu truy vấn cơ sở dữ liệu
        List<String> args = new ArrayList<>(); // Danh sách các tham số của câu truy vấn

        // Thêm điều kiện tìm kiếm theo Tên món ăn nếu có
        if (ten != null && !ten.isEmpty()) {
            query += " AND " + foodDB.COL_TEN + " LIKE ?";
            args.add("%" + ten + "%");
        }

        // Thêm điều kiện tìm kiếm theo khoảng giá nếu có
        if (giaTu >= 0 && giaDen >= 0 && giaTu <= giaDen) {
            query += " AND " + foodDB.COL_GIA + " BETWEEN ? AND ?";
            args.add(String.valueOf(giaTu));
            args.add(String.valueOf(giaDen));
        }

        // Thêm điều kiện tìm kiếm theo Danh mục nếu danhMuc không phải là "Tất cả"
        if (!danhMuc.equals("Tất cả") && !danhMuc.isEmpty()) {
            query += " AND " + foodDB.COL_DANHMUC + " = ?";
            args.add(danhMuc);
        }

        // Thực hiện truy vấn với các tham số đã xây dựng
        Cursor cursor = db.rawQuery(query, args.toArray(new String[0]));

        return cursor; // Trả về kết quả của truy vấn
    }

    // Xử lý kết quả trả về từ activity chọn hình ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData(); // Lấy địa chỉ Uri của hình ảnh đã chọn
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage); // Đọc hình ảnh từ Uri
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Nén hình ảnh vào luồng ByteArrayOutputStream
                photo = stream.toByteArray(); // Lưu hình ảnh dưới dạng mảng byte
                if (imgPreview != null) {
                    imgPreview.setImageBitmap(bitmap); // Hiển thị hình ảnh đã chọn trên ImageView
                } else {
                    Toast.makeText(this, "Lỗi: Không tìm thấy hình ảnh!", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo lỗi nếu không tìm thấy ImageView
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Tạo menu trong Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu); // Đổ menu từ file optionmenu.xml vào menu của Activity
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd) { // Nếu người dùng chọn menuAdd từ menu
            Dialog dialog = new Dialog(MainActivity.this); // Tạo đối tượng Dialog mới
            dialog.setContentView(R.layout.addfood); // Thiết lập layout cho Dialog từ file addfood.xml

            // Lấy các View trong Dialog để thêm món ăn mới
            EditText edtTen = dialog.findViewById(R.id.edtTen);
            EditText edtMoTa = dialog.findViewById(R.id.edtMoTa);
            EditText edtGia = dialog.findViewById(R.id.edtGia);
            Spinner spnDanhMuc = dialog.findViewById(R.id.spnDanhMuc);
            imgPreview = dialog.findViewById(R.id.imvHinhAnh); // Gán imgPreview ở đây để hiển thị hình ảnh xem trước

            // Thiết lập Spinner danh mục để người dùng chọn loại món ăn
            String[] danhMucArray = {"Khai vị", "Món chính", "Tráng miệng"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, danhMucArray);
            spnDanhMuc.setAdapter(adapter);

            // Xử lý khi người dùng nhấn nút Thêm hình ảnh
            Button btnThemHinhAnh = dialog.findViewById(R.id.btnThemHinhAnh);
            btnThemHinhAnh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1); // Mở activity để chọn hình ảnh từ bộ nhớ
                }
            });

            // Xử lý khi người dùng nhấn nút Thêm món ăn
            Button btnThemMonAn = dialog.findViewById(R.id.btnThemMonAn);
            btnThemMonAn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ten = edtTen.getText().toString();
                    String mota = edtMoTa.getText().toString();
                    double gia = Double.parseDouble(edtGia.getText().toString());
                    String danhMuc = spnDanhMuc.getSelectedItem().toString();

                    boolean inserted = foodDB.insertData(ten, mota, gia, danhMuc, photo); // Thêm dữ liệu món ăn vào cơ sở dữ liệu
                    if (inserted) {
                        loadData(); // Load lại dữ liệu sau khi thêm món ăn thành công
                        dialog.dismiss(); // Đóng Dialog sau khi thêm món ăn thành công
                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi: Hãy thêm lại", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo lỗi nếu thêm món ăn không thành công
                    }
                }
            });

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Thiết lập kích thước và hiển thị Dialog
            dialog.show();
        }
        return super.onOptionsItemSelected(item); // Trả về kết quả mặc định khi xử lý xong menu item
    }
}