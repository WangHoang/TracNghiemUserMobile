package com.example.tracnghiemuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tracnghiemuser.databinding.ActivitySignInBinding;
import com.example.tracnghiemuser.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {


    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Tạo tài khoản");
        dialog.setMessage("Tài khoản đang được tạo");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

                auth.createUserWithEmailAndPassword(binding.userEmail.getText().toString(), binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if(task.isSuccessful()) {
                                    // Lưu thông tin người dùng vào cơ sở dữ liệu
                                    String userId = task.getResult().getUser().getUid();
                                    HashMap<String, Object> userData = new HashMap<>();
                                    userData.put("userName", binding.userName.getText().toString());
                                    userData.put("userEmail", binding.userEmail.getText().toString());
                                    userData.put("password", binding.password.getText().toString());
                                    userData.put("score", 0); // Điểm khởi tạo là 0
                                    userData.put("rank", 1); // Rank khởi tạo là 1
                                    database.getReference().child("users").child(userId).setValue(userData);
                                    // Chuyển người dùng đến FirstActivity sau khi đăng ký thành công
                                    Intent intent = new Intent(SignUpActivity.this, FirstActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Xử lý khi đăng ký thất bại
                                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
