package com.example.de04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.adapter.SanphamAdapter;
import com.example.database.SanphamDB;
import com.example.de04.databinding.ActivityMainBinding;
import com.example.model.Sanphammodel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SanphamDB sanphamDB;
    SanphamAdapter sanphamAdapter;
    ArrayList<Sanphammodel> sanphamList;
    private byte[] photo = new byte[0];
    private ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDB();
        loadData();
        addEvents();
    }
    private void addEvents() {
        binding.lvSP.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add);
                //khong lam ma vi tu tang
                EditText edtName =dialog.findViewById(R.id.edtTen);
                EditText edtGia = dialog.findViewById(R.id.edtGia);
                Button btnSave = dialog.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtName.getText().toString();
                        double price = Double.parseDouble(edtGia.getText().toString());
                        sanphamDB.execSQL(" INSERT INTO " + SanphamDB.TBL_NAME + " VALUES(null, '" + name + "'," + price + ", null )");
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

    }
    private void prepareDB() {
        sanphamDB = new SanphamDB(MainActivity.this);
        sanphamDB.createSampleData();
    }

    private void loadData() {
        sanphamAdapter = new SanphamAdapter(MainActivity.this, R.layout.item, getDatadb());
        binding.lvSP.setAdapter(sanphamAdapter);
    }

    private List<Sanphammodel> getDatadb() {
        sanphamList = new ArrayList<>();
        Cursor cursor = sanphamDB.getData("SELECT * FROM " + SanphamDB.TBL_NAME);

        while (cursor.moveToNext()) {
            sanphamList.add(new
                    Sanphammodel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getBlob(3)));
        }
        cursor.close();
        return sanphamList;
    }
}