package com.example.lab5sever;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.icu.util.Output;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity {
    private TextView tvServerName, tvServerPort, tvStatus, tvReceivedMessage;
    private String serverIP = "192.168.0.103"; // ĐỊA CHỈ IP MÁY
    private int serverPort = 1234; // PORT
    private Button bntStart, bntStop, bntSend;
    private EditText edMessage;
    private ServerThread serverThread;
    // Sử dụng Handler để làm việc với giao diện trong Thread
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        tvServerName = findViewById(R.id.tvsevername);
        tvServerPort = findViewById(R.id.tvServerpory);
        tvStatus = findViewById(R.id.tvstatus);
        tvReceivedMessage = findViewById(R.id.tv_nhantinnhan);
        edMessage = findViewById(R.id.edtinhandi);
        bntStart = findViewById(R.id.bntstart);
        bntStop = findViewById(R.id.bntstop);
        bntSend = findViewById(R.id.bnt_sent);

        // Hiển thị địa chỉ IP và cổng của Server lên giao diện

        tvServerName.setText(serverIP);
        tvServerPort.setText(String.valueOf(serverPort));

        serverThread = new ServerThread();
        Toast.makeText(this, "SERVER ĐÃ CHẠY", Toast.LENGTH_SHORT).show();
        serverThread.startServer();

    }
    public void onClickStartServe(View view) {
        serverThread = new ServerThread();
        Toast.makeText(this, "SERVER ĐÃ Dừng", Toast.LENGTH_SHORT).show();
        serverThread.stopServer();
    }
    // Xử lý sự kiện nút "Dừng Server"

    public void onClickStopServe(View view) {
        if (serverThread != null) {
            serverThread.stopServer();
            Toast.makeText(this, "SERVER Dừng", Toast.LENGTH_SHORT).show();
        }
    }
    // Xử lý nút gửi
    public void onClickSend(View view) {
        String messageToSend = edMessage.getText().toString();
        if (!messageToSend.isEmpty() && serverThread != null) {
            serverThread.sendMessageToClients(messageToSend);
            edMessage.setText("");
        }
    }

    class ServerThread extends Thread {
        private boolean serverRunning;
        private ServerSocket serverSocket;
        // Phương thức start
        public void startServer() {
            serverRunning = true;
            start();
        }
        public void stopServer() {
            serverRunning = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvStatus.setText("Server stopped");
                }
            });
        }
        public void sendMessageToClients(final String message) {
            if (serverSocket != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (ClientHandler client : clientsList) {
                            client.sendMessageToClient(message);
                        }
                    }
                }).start();
            }
        }
        private ArrayList<ClientHandler> clientsList = new ArrayList<ClientHandler>();

        @Override
        public void run() {
            try {
                // Tạo Socket Server
                serverSocket = new ServerSocket(serverPort);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvStatus.setText("Waiting for Clients");
                    }
                });

                while (serverRunning) {
                    // Chấp nhận các kết nối từ Client
                    Socket socket = serverSocket.accept();
                    // Xử lý Client kết nối mới trong một luồng riêng biệt

                    ClientHandler client = new ClientHandler(socket);
                    client.start();
                    clientsList.add(client);
                    // Cập nhật giao diện khi có Client kết nối thành công

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvStatus.setText("Connected to: " + socket.getInetAddress() + " : " + socket.getLocalPort());
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        class ClientHandler extends Thread {
            private Socket clientSocket;
            private BufferedReader br_input;
            private PrintWriter output_Client;

            public ClientHandler(Socket socket) {
                clientSocket = socket;
                try {
                    // Lấy luồng đầu vào và luồng đầu ra của Client
                    br_input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    output_Client = new PrintWriter(clientSocket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Phương thức gửi tin nhắn đến Client
            public void sendMessageToClient(String message) {
                output_Client.println(message);
            }

            @Override
            public void run() {
                try {
                    // Hiển thị tin nhắn từ Client lên giao diện
                    String messageFromClient;
                    while ((messageFromClient = br_input.readLine()) != null) {
                        // Hiển thị tin nhắn từ Client lên giao diện
                        final String finalMessage = messageFromClient;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Set tin nhắn lên texview giao diện
                                tvReceivedMessage.setText("Tin nhắn từ client: " + finalMessage);

                                // thông báo co tin nhắn mới
                                final Dialog dialog = new Dialog(MainActivity.this);
                                // set layout dialog
                                dialog.setContentView(R.layout.dialog_thongbao);
                                TextView tvmes=dialog.findViewById(R.id.tvmess);
                                // set tin nhắn lên texviewở dialog
                                tvmes.setText(""+finalMessage);
                                dialog.setCancelable(true);
                                // show dialog
                                dialog.show();

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
        }
    }

}