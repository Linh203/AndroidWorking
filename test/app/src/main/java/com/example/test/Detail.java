package com.example.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.test.databinding.ActivityDetailBinding;

public class Detail extends AppCompatActivity {

    private Comic comic;
    private ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        comic = (Comic) getIntent().getSerializableExtra("comic");

        initViews();
    }

    private void initViews() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        Glide.with(this).load(comic.getImage()).error(R.drawable.i8).into(binding.imgArt);
        binding.tvName.setText(comic.getTitle());
        binding.tvAuthor.setText(comic.getContent());
        binding.tvContent.setText(comic.getEnd_date());
    }
}