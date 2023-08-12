package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import com.example.test.databinding.ActivityMainBinding;
import com.example.test.databinding.DialogAddBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<Comic> comics;
    private Comic comic;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();

    }

    private void initUI() {
        adapter = new Adapter(this);
        getAllComic();
        //cop tu adapter
        binding.fabAdd.setOnClickListener(view->{
            dialogAdd();
        });
    }

    private void dialogAdd() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        DialogAddBinding binding = DialogAddBinding.inflate(LayoutInflater.from(this));
        dialog.setView(binding.getRoot());
        AlertDialog alertDialog = dialog.create();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.color.white);

        binding.btnCancel.setOnClickListener(v->{
            binding.edtTitle.setText("");
            binding.edtContent.setText("");
            binding.edtImg.setText("");
            binding.edtStatus.setText("");
            binding.edtEndDate.setText("");
            alertDialog.dismiss();
        });
        binding.btnSave.setOnClickListener(v->{
            String title = binding.edtTitle.getText().toString();
            int status = Integer.parseInt(binding.edtStatus.getText().toString());
            String img = binding.edtImg.getText().toString();
            String content = binding.edtContent.getText().toString();
            String enddate = binding.edtEndDate.getText().toString();
            String formatDate= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                formatDate = currentDate.format(dateTimeFormatter);
            }
            comic = new Comic("", title, content, enddate, status, img , formatDate);
            addComic(comic);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void addComic(Comic comic) {
        Api.instance.addComic(comic).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                Toast.makeText(MainActivity.this, "Add successfully", Toast.LENGTH_SHORT).show();
                getAllComic();
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Add error", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllComic();
    }

    private void getAllComic() {
        Api.instance.getAllComic().enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                comics = response.body();
                binding.tvQuantity.setText("Total quantity: "+comics.size());
                adapter.setList(comics);
                binding.rcv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {

            }
        });
    }
}