package com.example.de04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.de04.databinding.ActivityMultiBinding;

import java.util.Random;

public class MultiActivity extends AppCompatActivity {
    ActivityMultiBinding binding;
    int percent ;
    int randNumb;

    // tạo biến random để in ra số ngẫu nhiên
    Random random = new Random();

    Handler handler = new Handler() ;
    LinearLayout rowLayout;

    //Main/Ui -Thread
    //     HIỂN THỊ SO LE SỐ
    Runnable foregroundThread = new Runnable() {
        @Override
        public void run() {

            //update ui
            binding.txtPercent.setText(percent+ "%");   // cập nhật nội dung cho textview hiển thị phần trăm
            binding.pbPercent.setProgress(percent);     // cập nhật nội dung cho progressbar hiển thị phần trăm

            // tạo một layout dùng để cài đặt vị trí, hình dáng của button hiển thị số ngẫu nhiên
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,15,15,15);
            if (rowLayout == null || rowLayout.getChildCount() == 3){
                rowLayout = new LinearLayout(MultiActivity.this);
                rowLayout.setLayoutParams(params);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                binding.container.addView(rowLayout);
            }
            Button button = new Button(MultiActivity.this);
            button.setTextSize(22);
            button.setText(String.valueOf(randNumb));

            int length = rowLayout.getWidth();
            int btnLength = length / 3;
            button.setWidth(btnLength);
            button.setHeight(100);
            rowLayout.addView(button);


            // kiểm tra số ngẫu nhiên có chia hết cho 2 hay không
            if(randNumb % 2 == 0){
                button.setBackgroundColor(Color.GREEN);     // nếu chia hết cho 2 thì thay đổi màu của button
            }else {
                button.setBackgroundColor(Color.GRAY);
            }

            // nếu phần trăm bằng 100 thì dừng thread
            if(percent == 100)
            {
                binding.txtPercent.setText("DONE!");    // cập nhật nội dung cho textview hiển thị DONE!
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiBinding.inflate(getLayoutInflater());
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
        // lấy dữ liệu từ edittext
        int numbOfViews = Integer.parseInt(binding.edtRand.getText().toString());

        //background / worker -thread
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // cho random số ngẫu nhiên với tổng số lượng bằng numbOfViews
                for(int i= 1; i<= numbOfViews; i++)
                {
                    percent = i*100/numbOfViews;    // tính phần trăm
                    randNumb= random.nextInt(9);      // lấy số ngẫu nhiên tối đa là 9
                    handler.post(foregroundThread);     // thực thi thread
                    SystemClock.sleep(100);     // dừng thread
                }
            }
        });
        backgroundThread.start();   // thực thi thread
    }

}