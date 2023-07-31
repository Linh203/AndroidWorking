package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView resultText;
    private static final String GET_URL = "http://103.118.28.46:3000/get-quote";
    private static final String GET_LIST_URL = "http://103.118.28.46:3000/get-list-quote";
    private static final String POST_URL = "http://103.118.28.46:3000/add-quote";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(com.example.myapplication.R.layout.activity_main);

        resultText = findViewById(R.id.txtview);
    }
    private void sendGetHttpUrlConnection() throws Exception {
        URL url = new URL(GET_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = conn.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);

            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject();
            String quote = getValue("quote", jsonReader);
            resultText.setText(quote);

        } else {
            // Xử lý phản hồi lỗi
            String error = getErrorMessage(conn);
            // Hiển thị thông báo lỗi lên TextView hoặc thực hiện các hành động phù hợp khác
            resultText.setText(error);
        }
    }
    private void sendPostHttpURLConnection() throws Exception {
        URL url = new URL(POST_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true);

        JSONObject data = new JSONObject();
        data.put("name", "Linh");

        byte[] postData = data.toString().getBytes(StandardCharsets.UTF_8);
        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(postData);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = conn.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);

            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject();
            String quote = getValue("message", jsonReader);
            resultText.setText(quote);

        } else {
            // Xử lý phản hồi lỗi
            String error = getErrorMessage(conn);
            // Hiển thị thông báo lỗi lên TextView hoặc thực hiện các hành động phù hợp khác
            resultText.setText(error);
        }
    }
    private void sendGetListHttpUrlConnection(int num) throws Exception {
        URL url = new URL(GET_LIST_URL + "?num=" + num);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = conn.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);

            JsonReader jsonReader = new JsonReader(responseBodyReader);

            List<String> quotes = new ArrayList<>();

            jsonReader.beginArray();
            for (int i = 0; i < num && jsonReader.hasNext(); i++) {
                if (jsonReader.peek() == JsonToken.STRING) {
                    String quote = jsonReader.nextString();
                    quotes.add(quote);
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endArray();

            StringBuilder sb = new StringBuilder();
            for (String quote : quotes) {
                sb.append(quote).append("\n");
            }

            resultText.setText(sb.toString());
        } else {
            // Xử lý phản hồi lỗi
            String error = getErrorMessage(conn);
            // Hiển thị thông báo lỗi lên TextView hoặc thực hiện các hành động phù hợp khác
            resultText.setText(error);
        }
    }
    private String getErrorMessage(HttpURLConnection conn) throws Exception {
        InputStream errorStream = conn.getErrorStream();
        if (errorStream != null) {
            InputStreamReader errorStreamReader = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(errorStreamReader);
            jsonReader.beginObject();
            String errorMessage = getValue("message", jsonReader);
            jsonReader.close();
            return errorMessage;
        } else {
            return "Unknown error";
        }
    }
    public void onClickSendGetListHttpUrlConnection(View view) {
        int num = 5; // Số câu nói cần lấy
        try {
            sendGetListHttpUrlConnection(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onClickSendPostHttpUrlConnection(View view) {
        try {
            sendPostHttpURLConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onClickSendGetHttpUrlConnection(View view) {
        try {
            sendGetHttpUrlConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getValue(String key, JsonReader jsonReader) throws Exception {
        String value = "";
        while (jsonReader.hasNext()) {
            String k = jsonReader.nextName();
            if (k.equals(key)) {
                value = jsonReader.nextString();
                break;
            } else {
                jsonReader.skipValue();
            }
        }
        return value;
    }
}