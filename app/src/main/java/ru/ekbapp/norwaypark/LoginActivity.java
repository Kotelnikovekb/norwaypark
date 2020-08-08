package ru.ekbapp.norwaypark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ekbapp.norwaypark.Class.User;
import ru.ekbapp.norwaypark.Module.JSONParser;

public class LoginActivity extends AppCompatActivity {

    public static final String SETTINGS_S="SETTINGS_S";
    public static final String SESSION_S="SESSION_S";
    public static final String USER_NAME_S="USER_NAME_S";
    public static final String OLL_PRODUCT_J="OLL_PRODUCT_J";

    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    String session;
    CardView cardLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences(SETTINGS_S,MODE_PRIVATE);
        progressBar=findViewById(R.id.progressBar);
        cardLogin=findViewById(R.id.cardLogin);
        final User user=new User(LoginActivity.this);


        final Button loginIn=findViewById(R.id.button);
        final EditText loginInput=findViewById(R.id.cardLoginInput);
        final EditText passwordInput=findViewById(R.id.passwordInput);
        session=sharedPreferences.getString(SESSION_S,"");
        if (session.equals("")){
            cardLogin.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            boolean u=user.loginUser(session);
            if (u){
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }else{
                progressBar.setVisibility(View.GONE);
                cardLogin.setVisibility(View.VISIBLE);
            }

        }
        loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginInput.getText().toString().equals("")&&!passwordInput.getText().toString().equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                    boolean u= user.loginUser(loginInput.getText().toString(),passwordInput.getText().toString());
                    if (u){
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else {
                        Snackbar.make(passwordInput,"Отказ смотри лог",Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    Snackbar.make(passwordInput,"Введите логин и пароль",Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

}