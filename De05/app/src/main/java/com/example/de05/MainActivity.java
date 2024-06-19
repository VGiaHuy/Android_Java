package com.example.de05;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.de05.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int count = 0;
    int randNumb;

    // tạo biến random để in ra số ngẫu nhiên
    Random random = new Random();

    Handler handler = new Handler();
    LinearLayout currentRow ;

    //Main/Ui -Thread
    //Main/Ui -Thread
    Runnable foregroundThread = new Runnable() {
        @Override
        public void run() {

            // Tạo một layout dùng để cài đặt vị trí, hình dáng của Button và EditText
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);

            Button button = new Button(MainActivity.this);
            button.setLayoutParams(params);
            button.setText(String.valueOf(randNumb));
            button.setTextSize(22);

            if (count % 2 == 0) {
                currentRow = new LinearLayout(MainActivity.this);

                currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                currentRow.setOrientation(LinearLayout.HORIZONTAL);

                binding.containerLayout.addView(currentRow);
            }
            if ((count / 2) % 2 == 0) {
                if (count % 2 == 0) {
                    params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
                } else {
                    params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 7);
                }
            } else {
                if (count % 2 == 0) {
                    params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 7);
                } else {
                    params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
                }

            }
            // kiểm tra số ngẫu nhiên có chia hết cho 2 hay không
            if(randNumb % 2 == 0){
                button.setBackgroundColor(Color.GREEN);     // nếu chia hết cho 2 thì thay đổi màu của button
            }else {
                button.setBackgroundColor(Color.GRAY);
            }
            params.setMargins(15, 15, 15, 15);
            button.setLayoutParams(params);

            currentRow.addView(button);
            count++;


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
        int numbOfViews = Integer.parseInt(binding.edtNumbofView.getText().toString());
        //background / worker -thread
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i= 1; i<= numbOfViews; i++)
                {
                    randNumb= random.nextInt(100);
                    handler.post(foregroundThread);
                    SystemClock.sleep(100);

                }
            }
        });
        backgroundThread.start();
    }


}