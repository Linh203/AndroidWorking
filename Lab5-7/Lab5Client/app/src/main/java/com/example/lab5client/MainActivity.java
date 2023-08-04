package com.example.lab5client;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    private TextView tvReceivedData, tvshowtinnhan;
    private EditText etServerName, etServerPort, edTinNhan;
    private Button bntClientConnect, bntSend;
    private String serverName;
    private int serverPort;
    private Socket socket;
    private BufferedReader br_input;
    private PrintWriter output_Client;
    private boolean connected = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvReceivedData = findViewById(R.id.tvshowReactxt);

        tvshowtinnhan = findViewById(R.id.tvshotinhan);

        etServerName = findViewById(R.id.edsevrname);

        etServerPort = findViewById(R.id.edseverport);

        bntClientConnect = findViewById(R.id.bntclient);

        edTinNhan = findViewById(R.id.editTextText);

        bntSend = findViewById(R.id.bntsenserver);
        // Xử lý sự kiện nút "Connect to Server"
        bntClientConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connected) {
                    // Khi chưa kết nối, lấy thông tin Server từ EditText và kết nối tới Server
                    serverName = etServerName.getText().toString();
                    serverPort = Integer.parseInt(etServerPort.getText().toString());
                    connectToServer(serverName, serverPort);
                } else {
                    // Khi đã kết nối, ngắt kết nối với Server
                    disconnectFromServer();
                }
            }
        });

        bntSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = edTinNhan.getText().toString();
                if (!messageToSend.isEmpty() && connected) {


                    // Gửi tin nhắn tới Server
                    sendMessageToServer(messageToSend);
                    edTinNhan.setText("");


                }
            }
        });
    }
    private void connectToServer(final String serverName, final int serverPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Tạo kết nối tới Server
                    socket = new Socket(serverName, serverPort);
                    br_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output_Client = new PrintWriter(socket.getOutputStream(), true);
                    // Cập nhật giao diện khi kết nối thành công
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvReceivedData.setText("Connected to Server");
                            bntClientConnect.setText("Disconnect");
                            connected = true;
                        }
                    });
                    // Nhận tin nhắn từ Server và hiển thị lên giao diện
                    String messageFromServer;
                    while ((messageFromServer = br_input.readLine()) != null) {
                        final String finalMessage = messageFromServer;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvshowtinnhan.setText("Thông Báo từ sever: Yêu cầu lưu " + finalMessage+" ảnh !");
                                // thông báo co tin nhắn mới
                                final Dialog dialog = new Dialog(MainActivity.this);
                                // set layout dialog
                                dialog.setContentView(R.layout.dialog_thongbao);
                                TextView tvmes=dialog.findViewById(R.id.tvtin);
                                // set tin nhắn lên texviewở dialog
                                tvmes.setText(""+finalMessage);
                                dialog.setCancelable(true);
                                // show dialog
//                                dialog.show();
                                sendMessageToServer("Đã lưu thêm "+ finalMessage +" ảnh vào sever !");
                                // set dialog sau 4s thì ẩn
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                }, 4000);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void disconnectFromServer() {
        try {
            if (socket != null) {
                socket.close();
                connected = false;

                bntClientConnect.setText("Connect to Server");
                tvReceivedData.setText("Disconnected");
                tvshowtinnhan.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessageToServer(final String message) {
        if (output_Client != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    output_Client.println(message);
                }
            }).start();
        }
    }
}