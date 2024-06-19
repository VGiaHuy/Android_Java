package com.example.de05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adapter.SachAdapter;
import com.example.database.SachDB;
import com.example.de05.databinding.ActivitySqliteBinding;
import com.example.model.Sach;

import java.util.ArrayList;
import java.util.List;

public class SqliteActivity extends AppCompatActivity {

    ActivitySqliteBinding binding;
    SachDB sanphamDB;
    SachAdapter sanphamAdapter;
    ArrayList<Sach> sachArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySqliteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDB();
        loadData();
        addEvents();
    }
    private void addEvents() {
        ///sửa
        binding.lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Sach p = (Sach) parent.getItemAtPosition(position);

                Dialog dialog = new Dialog(SqliteActivity.this);
                dialog.setContentView(R.layout.dialog);

                // Đặt thông tin sản phẩm lên các TextView
                TextView edtMa = dialog.findViewById(R.id.edtMa);
                EditText edtTen = dialog.findViewById(R.id.edtTen);
                EditText edtGia = dialog.findViewById(R.id.edtGia);

                edtMa.setText(String.valueOf(p.getMasp()));
                edtTen.setText(p.getTensp());
                edtGia.setText(String.valueOf(p.getGiasp()));

                Button btnSave = dialog.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lấy giá trị mới từ EditText
                        String newTen = edtTen.getText().toString();
                        String newGia = edtGia.getText().toString();

                        // Kiểm tra nếu giá trị mới khác với giá trị cũ thì mới cập nhật
                        if (!newTen.equals(p.getTensp()) || !newGia.equals(String.valueOf(p.getGiasp()))) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SqliteActivity.this);
                            builder.setTitle("Xác nhận cập nhật");
                            builder.setIcon(android.R.drawable.ic_dialog_info);
                            builder.setMessage("Bạn có chắc muốn cập nhật sản phẩm: " + p.getTensp() + "?");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Thực hiện câu lệnh SQL cập nhật sản phẩm
                                    SachDB sachDB = new SachDB(SqliteActivity.this); // Assuming SachDB is your database helper class
                                    sachDB.execSQL("UPDATE " + SachDB.TBL_NAME + " SET "
                                            + SachDB.COL_TENSP + " = '" + newTen + "', "
                                            + SachDB.COL_GIASP + " = '" + newGia + "' "
                                            + "WHERE " + SachDB.COL_MASP + " = '" + p.getMasp() + "'");
                                    loadData(); // Tải lại dữ liệu sau khi cập nhật
                                    dialogInterface.dismiss(); // Đóng AlertDialog xác nhận cập nhật
                                    dialog.dismiss(); // Đóng dialog chi tiết
                                }
                            });

                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss(); // Đóng AlertDialog xác nhận cập nhật
                                }
                            });

                            // Tạo và hiển thị AlertDialog xác nhận cập nhật
                            AlertDialog confirmDialog = builder.create();
                            confirmDialog.show();
                        } else {
                            // Nếu không có thay đổi thì chỉ cần đóng dialog
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show(); // Hiển thị dialog chi tiết sản phẩm
            }
        });

//lưu
        /*binding.lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(SqliteActivity.this);
                dialog.setContentView(R.layout.dialog);

                //khong lam ma vi tu tang
                EditText edtMa =dialog.findViewById(R.id.edtMa);
                EditText edtTen = dialog.findViewById(R.id.edtTen);
                EditText edtGia = dialog.findViewById(R.id.edtGia);

                Button btnSave = dialog.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ma = edtMa.getText().toString();
                        String name = edtTen.getText().toString();
                        double price = Double.parseDouble(edtGia.getText().toString());
                        sanphamDB.execSQL(" INSERT INTO " + SachDB.TBL_NAME + " VALUES('" + ma + "', '" + name + "'," + price + " )");
                        loadData();
                        dialog.dismiss();
                    }
                });

                Button btnCancel = dialog.findViewById(R.id.btnThoat);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
*/
    }
    private void prepareDB() {
        sanphamDB = new SachDB(SqliteActivity.this);
        sanphamDB.createSampleData();
    }

    private void loadData() {
        sanphamAdapter = new SachAdapter(SqliteActivity.this, R.layout.itemsach, getDatadb());
        binding.lvSanPham.setAdapter(sanphamAdapter);
    }

    private List<Sach> getDatadb() {
        sachArrayList = new ArrayList<>();
        Cursor cursor = sanphamDB.getData("SELECT * FROM " + SachDB.TBL_NAME);

        while (cursor.moveToNext()) {
            sachArrayList.add(new
                    Sach(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getDouble(2)));
        }
        cursor.close();
        return sachArrayList;
    }

}