package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    TextInputEditText editemail, editpass;
    Button resgis;

    private FirebaseAuth mAuth;

    ProgressBar progressBar;
    private TextView textView;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(Register.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editemail = findViewById(R.id.email);
        editpass = findViewById(R.id.pass);
        resgis = findViewById(R.id.regis);
        progressBar=findViewById(R.id.progressbar);
        textView=findViewById(R.id.loginNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        resgis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass;
                email = String.valueOf(editemail.getText());
                pass = String.valueOf(editpass.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Nhập Email !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Register.this, "Nhập PassWord !", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Đăng Ký Thành Công !",Toast.LENGTH_SHORT).show();
                            editemail.setText("");
                            editpass.setText("");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Đăng Ký Không Thành Công !",Toast.LENGTH_SHORT).show();
                            editemail.setText("");
                            editpass.setText("");
                        }
                    }
                });
            }
        });
    }
}