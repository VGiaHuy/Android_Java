package com.example.de02;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adapter.SanPhamAdapter;
import com.example.database.SanPhamDB;
import com.example.de02.databinding.ActivitySqliteBinding;
import com.example.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class SqliteActivity extends AppCompatActivity {
    ActivitySqliteBinding binding;
    SanPhamDB sanPhamDB;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> sanPhamArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySqliteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDb(); // Chuẩn bị cơ sở dữ liệu và tạo dữ liệu mẫu
        loadData(); // Load dữ liệu từ cơ sở dữ liệu lên ListView
        addEvents(); // Xử lý sự kiện khi người dùng tương tác trên giao diện
    }

    private void addEvents() {
        binding.lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final SanPham p = (SanPham) parent.getItemAtPosition(position);

                Dialog dialog = new Dialog(SqliteActivity.this);
                dialog.setContentView(R.layout.dialog);

                // Đặt thông tin sản phẩm lên các TextView
                TextView txtMa = dialog.findViewById(R.id.txtMa);
                TextView txtTen = dialog.findViewById(R.id.txtTen);
                TextView txtGia = dialog.findViewById(R.id.txtGia);

                txtMa.setText(String.valueOf(p.getMaSP()));
                txtTen.setText(p.getTenSP());
                txtGia.setText(String.valueOf(p.getGiaSP()));

                // Nút xóa
                Button btnDelete = dialog.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Tạo AlertDialog xác nhận xóa
                        AlertDialog.Builder builder = new AlertDialog.Builder(SqliteActivity.this);
                        builder.setTitle("Xác nhận xóa"); // Đặt tiêu đề
                        builder.setIcon(android.R.drawable.ic_delete); // Đặt icon
                        builder.setMessage("Bạn có chắc muốn xóa sản phẩm: " + p.getTenSP() + "?"); // Đặt thông báo

                        // Xử lý khi nhấn nút Ok
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Thực hiện câu lệnh SQL xóa sản phẩm
                                sanPhamDB.execSQL("DELETE FROM " + SanPhamDB.TBL_NAME + " WHERE " + SanPhamDB.COL_MA + " = '" + p.getMaSP() + "'");
                                loadData(); // Tải lại dữ liệu sau khi xóa
                                dialogInterface.dismiss(); // Đóng AlertDialog xác nhận xóa
                                dialog.dismiss(); // Đóng dialog chi tiết
                            }
                        });

                        // Xử lý khi nhấn nút Hủy
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss(); // Đóng AlertDialog xác nhận xóa
                            }
                        });

                        // Tạo và hiển thị AlertDialog xác nhận xóa
                        AlertDialog confirmDialog = builder.create();
                        confirmDialog.show();
                    }
                });

                Button btnCancel = dialog.findViewById(R.id.btnThoat);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                // Hiển thị dialog
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                return true;
            }
        });
    }


    private void prepareDb() {
        sanPhamDB = new SanPhamDB(this); // Khởi tạo đối tượng quản lý cơ sở dữ liệu
        sanPhamDB.createSampleData();
    }
    private void loadData() {
        sanPhamAdapter = new SanPhamAdapter(SqliteActivity.this, R.layout.itemsp, getDataFromDb());
        binding.lvSanPham.setAdapter(sanPhamAdapter);
    }

    private List<SanPham> getDataFromDb() {
        sanPhamArrayList = new ArrayList<>(); // Khởi tạo danh sách món ăn
        Cursor cursor = sanPhamDB.getData("SELECT * FROM " + SanPhamDB.TBL_NAME); // Truy vấn và lấy dữ liệu từ cơ sở dữ liệu

        while (cursor.moveToNext()) {
            sanPhamArrayList.add(new
                    SanPham(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getDouble(2)));
        }
        cursor.close(); // Đóng Cursor sau khi đã sử dụng xong
        return sanPhamArrayList; // Trả về danh sách món ăn
    }

}