package com.example.de05;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.de05.Adapters.SachAdapters;
import com.example.de05.Database.SachDB;
import com.example.de05.Model.Sach;
import com.example.de05.databinding.ActivitySqliteBinding;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends AppCompatActivity {
    ActivitySqliteBinding binding;
    SachDB db;
    SachAdapters adapter;
    ArrayList<Sach> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySqliteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDB();
        loadDB();
        addEvents();
    }

    private void addEvents() {
        binding.lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(SQLite.this);
                dialog.setContentView(R.layout.item_dialog);
                EditText edtName = dialog.findViewById(R.id.edTenSp);
                EditText edtGia = dialog.findViewById(R.id.edGiaSp);
                Button btnSave = dialog.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtName.getText().toString();
                        double price = Double.parseDouble(edtGia.getText().toString());
                        db.execSql(" INSERT INTO " + SachDB.TABLE_NAME + " VALUES(null, '" + name + "'," + price + ")");
                        loadDB();
                        dialog.dismiss();
                    }
                });

                Button btnCancel = dialog.findViewById(R.id.btnExit);
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

    private void loadDB() {
        adapter = new SachAdapters(SQLite.this, R.layout.item_custom, getDataFromDB());
        binding.lvProduct.setAdapter(adapter);
    }

    private List<Sach> getDataFromDB() {
        listProduct = new ArrayList<>();
        Cursor cursor = db.getData("SELECT * FROM " + SachDB.TABLE_NAME);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                listProduct.add(new Sach(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2)));
            }
            cursor.close();
        }
        return listProduct;
    }

    private void prepareDB() {
        db = new SachDB(this);
        db.CreateSampleData();
    }
}