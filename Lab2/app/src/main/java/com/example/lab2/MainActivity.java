package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String HASH = "a918c858d2dd1a3c69163267468804bdcd67daf50de8899183efe63e8412438a";
    private Button _myButton;
    private TextView _textNumber;

    private Handler _handler;

    private Integer messageNumber;

    private static final int MESSAGE_DONE = 101;

    private static final int MESSAGE_PROCESS = 100;

    private boolean isProcessing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _textNumber = findViewById(R.id.text_number);
        Init();
        listenerHandler();
        String result = decodeHash(HASH);
        Log.d("MainActivity" , result);

    }
    private static String decodeHash(String hash) {
        StringBuilder result = new StringBuilder();
        char[] characters = getValidCharacters();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(hash.getBytes());
            for (byte b : bytes) {
                int index = Math.abs(b) % characters.length;
                result.append(characters[index]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result.toString().substring(0,8);
    }

    private static char[] getValidCharacters() {
        StringBuilder characters = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) {
            characters.append(c);
        }
        return characters.toString().toCharArray();
    }

    protected void Init(){
        _textNumber = findViewById(R.id.text_number);
        _myButton = findViewById(R.id.button_number);
        _textNumber.setText("Hãy Bấm Nút !");
        _myButton.setOnClickListener(view -> countNumber());
    }

    private void countNumber(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++){
                    Message message = new Message();
                    message.what = MESSAGE_PROCESS;
                    message.arg1 = i;

                    _handler.sendMessage(message);
                    if ( i == 10){
                        isProcessing = false;
                    }
                    if (!isProcessing){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                _handler.sendEmptyMessage(MESSAGE_DONE);
            }
        }).start();
    }




    private void listenerHandler(){
        String result = decodeHash(HASH);
        _handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case MESSAGE_DONE:
                        _textNumber.setText("Thread is DONE"+" "+" chuỗi là :"+" "+result);
                        break;
                    case MESSAGE_PROCESS:
                      //  _textNumber.setText("Thread is running :" +result+ " "+String.valueOf(msg.arg1));
                        _textNumber.setText("Thread"+" "+String.valueOf(msg.arg1)+" chuỗi là :"+" "+result);

                        break;

                    default:
                        _textNumber.setText("Undefined message");
                        break;
                }
            }
        };
    }

}