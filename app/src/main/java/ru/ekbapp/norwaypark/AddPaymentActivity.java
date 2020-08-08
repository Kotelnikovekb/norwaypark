package ru.ekbapp.norwaypark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.ekbapp.norwaypark.Class.Product;

public class AddPaymentActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    List<String> categoryList=new ArrayList<>();
    List<Product> tovarList=new ArrayList<>();
    JSONArray objectJson;
    int step=0;
    double cost=0;
    String category;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("title"));
        setContentView(R.layout.activity_add_payment);

        relativeLayout=findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try{
            objectJson=new JSONArray(getIntent().getStringExtra("list"));
            for (int i=0;i<objectJson.length();i++){
                JSONObject obj=objectJson.getJSONObject(i);
                if (!categoryList.contains(obj.getString("category"))){
                    categoryList.add(obj.getString("category"));
                }
                tovarList.add(new Product(
                        obj.getString("type"),
                        obj.getString("category"),
                        obj.getString("name"),
                        obj.getString("cost")
                ));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        relativeLayout.addView(gridView(categoryList));
    }
    GridView gridView(final List<String> list){
        Log.e("tovar",list.size()+"");
        GridView gridView=new GridView(this);
        gridView.setNumColumns(2);
        final ArrayAdapter<String> gridViewArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, list);
        gridView.setAdapter(gridViewArrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickEvent(list.get(position));
            }
        });
        return gridView;
    }
    void clickEvent(String value){
        switch (step){
            case 0:
                category=value;
                step++;
                relativeLayout.removeAllViews();
                List<String> list=new ArrayList<>();
                for (int i=0;i<tovarList.size();i++){
                    if (tovarList.get(i).getCategory().equals(value)){
                        list.add(tovarList.get(i).getName());
                    }
                }
                relativeLayout.addView(gridView(list));
                break;
            case 1:
                name=value;
                step++;
                relativeLayout.removeAllViews();
                relativeLayout.addView(productCard());
                break;
        }
    }
    CardView productCard(){
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final CardView cardView = new CardView(this);
        cardView.setLayoutParams(lparams);

        final LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setLayoutParams(lparams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);


        TextView total=new TextView(this);
        total.setLayoutParams(lparams);
        total.setText("Товар: "+name);
        final TextView price=new TextView(this);
        price.setLayoutParams(lparams);
        price.setText("0 руб");
        final TextView descriptionTV=new TextView(this);
        descriptionTV.setLayoutParams(lparams);


        final TextInputLayout UserTIL =new TextInputLayout(this);
        UserTIL.setLayoutParams(lparams);
        final TextInputEditText UserText=new TextInputEditText(this);
        UserText.setLayoutParams(lparams);
        UserText.setHint("Укажите количество");

        UserText.setInputType(InputType.TYPE_CLASS_NUMBER);
        double priceOne=0;
        double mass=0;
        for (int i=0;i<tovarList.size();i++){
            if (tovarList.get(i).getName().equals(name)){
                priceOne=Double.parseDouble(tovarList.get(i).getCost());
            }
        }
        final double finalPriceOne = priceOne;
        UserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!UserText.getText().toString().equals("")){
                    try{
                        cost=Double.parseDouble(UserText.getText().toString())* finalPriceOne;
                        price.setText("Итого: "+cost+" руб.");
                    }catch (NumberFormatException error){
                        error.printStackTrace();
                    }
                }
            }
        });
        Button button = new Button(this);
        button.setText("Создать");
        button.setLayoutParams(lparams);
        button.setPadding(0,30,0,30);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserText.getText().toString().equals("")){
                    Toast.makeText(AddPaymentActivity.this, "Укажите стоимость", Toast.LENGTH_SHORT).show();
                }else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name",name);
                    returnIntent.putExtra("cost",cost);
                    returnIntent.putExtra("finalPriceOne",finalPriceOne);
                    returnIntent.putExtra("mass",Double.parseDouble(UserText.getText().toString()));
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });


        linearLayout.addView(total);
        UserTIL.addView(UserText);
        linearLayout.addView(UserTIL);
        linearLayout.addView(price);
        linearLayout.addView(descriptionTV);
        linearLayout.addView(button);

        cardView.addView(linearLayout);


        return cardView;

    }
}