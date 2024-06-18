package com.example.de2;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.de2.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int randNumb;
    int count = 1;  // dùng để tính thứ tự chẵn lẻ của view

    // tạo biến random để in ra số ngẫu nhiên
    Random random = new Random();
    Handler handler = new Handler() ;

    Runnable foregroundThread = new Runnable() {
        @Override
        public void run() {

            // tạo một layout dùng để cài đặt vị trí, hình dáng của button hiển thị số ngẫu nhiên
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);


             // kiểm tra số ngẫu nhiên có chia hết cho 2 hay không
            if(count % 2 == 0){
                Button button = new Button(MainActivity.this);
                button.setTextSize(22);
                button.setText(String.valueOf(randNumb));

                button.setLayoutParams(params);     // cài đặt layout cho button sau khi kiểm tra
                binding.containerLayout.addView(button);        // thêm button vào layout
            }
            else
            {
                EditText edt = new EditText(MainActivity.this);
                edt.setTextSize(22);
                edt.setText(String.valueOf(randNumb));

                edt.setLayoutParams(params);     // cài đặt layout cho button sau khi kiểm tra
                binding.containerLayout.addView(edt);        // thêm button vào layout
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvent();
    }

    private void addEvent() {
        // cài đặt sự kiện khi nhấn vào button draw
        binding.btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerLayout.removeAllViews();   // xóa tất cả các button trong layout
                execBackgroundThread();     // thực thi thread
            }
        });

    }

    private void execBackgroundThread() {
        // lấy dữ liệu từ edittext
        int numbOfViews = Integer.parseInt(binding.edtNumbofView.getText().toString());

        //background / worker -thread
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // cho random số ngẫu nhiên với tổng số lượng bằng numbOfViews
                for(int i= 1; i<= numbOfViews; i++)
                {
                    randNumb= random.nextInt(100);      // lấy số ngẫu nhiên tối đa là 100
                    handler.post(foregroundThread);     // thực thi thread
                    count++;    // cộng cout để tính thứ tự chẵn lẻ
                    SystemClock.sleep(100);     // dừng thread
                }
            }
        });
        backgroundThread.start();   // thực thi thread
    }
}