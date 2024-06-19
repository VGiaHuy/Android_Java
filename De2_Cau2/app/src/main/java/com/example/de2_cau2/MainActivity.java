package com.example.de2_cau2;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.de2_cau2.Adapters.SanPhamAdapters;
import com.example.de2_cau2.Database.SanPhamDB;
import com.example.de2_cau2.Model.SanPham;
import com.example.de2_cau2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SanPhamDB db;
    SanPhamAdapters adapter;
    ArrayList<SanPham> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDB();
        loadDB();

    }

    public void EventsLongClick(SanPham sp) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.item_dialog);

                EditText edtMa = dialog.findViewById(R.id.edt_maSanPham);
                edtMa.setText(String.valueOf(sp.getMa()));

                EditText edtTen = dialog.findViewById(R.id.edt_tenSanPham);
                edtTen.setText(sp.getTen());

                EditText edtGia = dialog.findViewById(R.id.edt_giaSanPham);
                edtGia.setText(String.valueOf(sp.getGia()));

                Button btn_xoaSanPham = dialog.findViewById(R.id.btn_xoaSanPham);
                Button btn_back = dialog.findViewById(R.id.btn_back);

                btn_xoaSanPham.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ma = edtMa.getText().toString();
                        db.execSql("DELETE FROM " + SanPhamDB.TABLE_NAME + " WHERE " + SanPhamDB.PD_ID + " = " + ma);
                        loadDB();
                        dialog.dismiss();
                    }
                });

                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

    }

    private void loadDB() {
        adapter = new SanPhamAdapters(MainActivity.this, R.layout.item_custom,getDataFromDB());
        binding.lvSanPham.setAdapter(adapter);
    }

    private List<SanPham> getDataFromDB() {
        listProduct = new ArrayList<>();
        Cursor cursor = db.getData("SELECT * FROM "+ SanPhamDB.TABLE_NAME);
        if (cursor != null){
            while (cursor.moveToNext()){
                listProduct.add(new SanPham(cursor.getInt(0),cursor.getString(1),cursor.getFloat(2)));
            }
            cursor.close();
        }
        return listProduct;
    }

    private void prepareDB() {
        db = new SanPhamDB(this);
        db.CreateSampleData();
    }
}