package ru.ekbapp.norwaypark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.ekbapp.norwaypark.Class.Product;
import ru.ekbapp.norwaypark.Class.User;

import static ru.ekbapp.norwaypark.LoginActivity.SETTINGS_S;
import static ru.ekbapp.norwaypark.LoginActivity.USER_NAME_S;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    User user;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user =new User(MainActivity.this);
        TextView title=findViewById(R.id.textView);
        title.setText("Привет "+user.USER_NAME+"!");
        product=new Product(MainActivity.this);
        product.updateProductList();
        CardView newOperation=findViewById(R.id.newOperation);
        newOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PaymentActivity.class).putExtra(PaymentActivity.TYPE_KEY,"Доход"));
            }
        });



    }
}
