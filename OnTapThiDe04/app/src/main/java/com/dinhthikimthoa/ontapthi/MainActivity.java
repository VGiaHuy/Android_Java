package com.dinhthikimthoa.ontapthi;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dinhthikimthoa.ontapthi.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int  randNumb;
    Random random = new Random();
    LinearLayout rowLayout;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int randNumb = (int) msg.obj;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,15,15,15);
            if (rowLayout == null || rowLayout.getChildCount() == 3){
                rowLayout = new LinearLayout(MainActivity.this);
                rowLayout.setLayoutParams(params);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                binding.container.addView(rowLayout);
            }
            Button button = new Button(MainActivity.this);
            button.setTextSize(22);
            button.setText(String.valueOf(randNumb));
            int length = rowLayout.getWidth();
            int btnLength = length / 3;
            button.setWidth(btnLength);
            button.setHeight(100);
            rowLayout.addView(button);
            if (randNumb % 2 == 0 ){
                button.setBackgroundColor(Color.rgb(0,0,246));
            }else {
                button.setBackgroundColor(Color.rgb(128,128,128));
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEvents();
    }
    private void addEvents() {
        binding.btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execBackgroundThread();
            }
        });
    }

    private void execBackgroundThread() {
        binding.container.removeAllViews();
        int numbOfViews = Integer.parseInt(binding.edtEnter.getText().toString());
        Thread backGroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i<=numbOfViews; i++){
                    Message message = handler.obtainMessage();
                    message.obj = random.nextInt(10);
                    handler.sendMessage(message);
                    SystemClock.sleep(100);
                }
            }
        });
        backGroundThread.start();
    }
}