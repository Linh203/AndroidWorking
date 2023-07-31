package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import java.math.BigInteger;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        String hash = "a918c858d2dd1a3c69163267468804bdcd67daf50de8899183efe63e8412438a";
        // Chuyển đổi mã hash từ hex thành byte array
        byte[] bytes = new BigInteger(hash).toByteArray();

        // Chuyển đổi byte array thành chuỗi hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x b"));
        }

        // Chuyển đổi chuỗi hex thành chuỗi kí tự từ a đến z
        String result = "";
        for (int i = 0; i < hexString.length(); i++) {
            char c = (char) ((hexString.charAt(i) % 26) + 'a');
            result += c;
        }

        // In kết quả
        Log.d("MainActivity2","Result:" + result);

    }
}