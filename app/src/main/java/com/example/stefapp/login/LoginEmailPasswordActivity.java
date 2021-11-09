package com.example.stefapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stefapp.MainActivity;
import com.example.stefapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmailPasswordActivity extends AppCompatActivity {
    private EditText editTextEmailAddress, editTextPassword, editTextEmailAddressForgot;
    private TextView textViewForgotPassword;
    private Button buttonClickSignUp, buttonClickSignIn, buttonRestorePassword, buttonBack;
    private FirebaseAuth mAuth;
    private FirebaseUser cUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_password);
        init();
    }
    @Override
    protected void onStart() {
        super.onStart();
        cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            if(cUser.isEmailVerified()){
                Intent i = new Intent(LoginEmailPasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(LoginEmailPasswordActivity.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginEmailPasswordActivity.this, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextEmailAddressForgot= findViewById(R.id.editTextEmailAddressForgot);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonClickSignUp = findViewById(R.id.buttonClickSignUp);
        buttonClickSignIn = findViewById(R.id.buttonClickSignIn);
        buttonRestorePassword = findViewById(R.id.buttonRestorePassword);
        buttonBack = findViewById(R.id.buttonBack);

        mAuth = FirebaseAuth.getInstance();
    }



    public void onClickSignUp(View view) {
        if (!TextUtils.isEmpty(editTextEmailAddress.getText().toString()) && !TextUtils.isEmpty(editTextPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(editTextEmailAddress.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                sendEmailVer();
                                assert user != null;
                                if(user.isEmailVerified()){
                                    Intent i = new Intent(LoginEmailPasswordActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(LoginEmailPasswordActivity.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginEmailPasswordActivity.this, "Не удалось зарегистрироваться", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginEmailPasswordActivity.this, "Введите адрес электронной почты и пароль", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailVer() {

        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginEmailPasswordActivity.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginEmailPasswordActivity.this, "Отправка не удалась", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onClickSignIn(View view) {
        if (!TextUtils.isEmpty(editTextEmailAddress.getText().toString()) && !TextUtils.isEmpty(editTextPassword.getText().toString())) {
            mAuth.signInWithEmailAndPassword(editTextEmailAddress.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Intent i = new Intent(LoginEmailPasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginEmailPasswordActivity.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginEmailPasswordActivity.this, "Не удалось войти в приложение", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(LoginEmailPasswordActivity.this, "Для входа введите имя пользователя и пароль", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickForgotPassword(View view) {
        editTextEmailAddress.setVisibility(View.INVISIBLE);
        editTextPassword.setVisibility(View.INVISIBLE);
        buttonClickSignUp.setVisibility(View.INVISIBLE);
        buttonClickSignIn.setVisibility(View.INVISIBLE);
        textViewForgotPassword.setVisibility(View.INVISIBLE);

        editTextEmailAddressForgot.setVisibility(View.VISIBLE);
        buttonRestorePassword.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);



    }


    public void restorePassword(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Editable emailAddress = editTextEmailAddressForgot.getText();

        auth.sendPasswordResetEmail(String.valueOf(emailAddress))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginEmailPasswordActivity.this, "Ссылка для восстановления пароля направлена на указанную почту", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Back(View view) {
        editTextEmailAddress.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        buttonClickSignUp.setVisibility(View.VISIBLE);
        buttonClickSignIn.setVisibility(View.VISIBLE);
        textViewForgotPassword.setVisibility(View.VISIBLE);

        buttonRestorePassword.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE);
        editTextEmailAddressForgot.setVisibility(View.INVISIBLE);

    }
}