package com.example.de02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.de02.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int percent;
    int randNumb;

    // tạo biến random để in ra số ngẫu nhiên
    Random random = new Random();

    Handler handler = new Handler();
    LinearLayout rowLayout;

    private int currentIndex;

    //Main/Ui -Thread
    //Main/Ui -Thread
    Runnable foregroundThread = new Runnable() {
        @Override
        public void run() {
            // Cập nhật UI
            binding.txtPercent.setText(percent + "%");   // Cập nhật nội dung cho TextView hiển thị phần trăm
            binding.pbPercent.setProgress(percent);     // Cập nhật nội dung cho ProgressBar hiển thị phần trăm

            // Tạo một layout dùng để cài đặt vị trí, hình dáng của Button và EditText
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);

            // Nếu rowLayout chưa được khởi tạo hoặc số lượng con của rowLayout là 3, tạo một LinearLayout mới
            if (rowLayout == null || rowLayout.getChildCount() == 1) {
                rowLayout = new LinearLayout(MainActivity.this);
                rowLayout.setLayoutParams(params);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                binding.container.addView(rowLayout);
            }

            // Lấy kích thước màn hình
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;

            // Tùy theo giá trị của currentIndex, tạo Button hoặc EditText
            if (currentIndex % 2 == 0) {
                // Vị trí chẵn: Tạo Button mới
                Button button = new Button(MainActivity.this);
                button.setTextSize(22);
                button.setText(String.valueOf(randNumb));  // Đặt số ngẫu nhiên làm text của Button
                button.setWidth(screenWidth);  // Thiết lập chiều rộng bằng màn hình
                button.setHeight(150);
                rowLayout.addView(button);  // Thêm Button vào rowLayout
            } else {
                // Vị trí lẻ: Tạo EditText mới
                EditText editText = new EditText(MainActivity.this);
                editText.setTextSize(22);
                editText.setText(String.valueOf(randNumb));  // Đặt số ngẫu nhiên làm text của EditText
                editText.setWidth(screenWidth);  // Thiết lập chiều rộng bằng  màn hình
                editText.setHeight(150);
                rowLayout.addView(editText);  // Thêm EditText vào rowLayout
            }
            if (percent == 100) {  // Kiểm tra nếu tiến trình hoàn thành
                binding.txtPercent.setText("DONE !");  // Cập nhật text là "DONE!"
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
                binding.container.removeAllViews();   // xóa tất cả các button trong layout
                execBackgroundThread();     // thực thi thread
            }
        });

    }

    private void execBackgroundThread() {
        // Lấy dữ liệu từ EditText
        int numbOfViews = Integer.parseInt(binding.edtNumb.getText().toString());
// Kiểm tra điều kiện nhập
        if (numbOfViews <= 0) {
            Toast.makeText(this, "Vui lòng nhập số lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }
        // Background / worker-thread
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Cho random số ngẫu nhiên với tổng số lượng bằng numbOfViews
                for (int i = 1; i <= numbOfViews; i++) {
                    if (percent >= 100) {
                        break;  // Nếu percent đã đạt 100%, thoát khỏi vòng lặp
                    }
                    percent = i * 100 / numbOfViews;  // Tính phần trăm
                    randNumb = random.nextInt(100);  // Lấy số ngẫu nhiên tối đa là 100

                    // Post lên UI thread
                    currentIndex = i;  // Lưu giá trị i hiện tại vào biến toàn cục
                    handler.post(foregroundThread);  // Gọi foregroundThread

                    // Dừng thread
                    SystemClock.sleep(100);
                }
            }
        });
        backgroundThread.start();  // Thực thi thread
    }


}