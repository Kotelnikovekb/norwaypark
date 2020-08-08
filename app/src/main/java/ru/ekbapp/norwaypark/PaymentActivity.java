package ru.ekbapp.norwaypark;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.ekbapp.norwaypark.Class.Product;
import ru.ekbapp.norwaypark.Class.User;

public class PaymentActivity extends AppCompatActivity {
    String TYPE;
    public static String TYPE_KEY="TYPE";
    List<Product> list=new ArrayList<>();
    ListView listView;
    ArrayAdapter<Product> adapter;
    TextView cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE=getIntent().getStringExtra(TYPE_KEY);
        setTitle("Новый "+TYPE);
        setContentView(R.layout.activity_payment);
        listView=findViewById(R.id.productList);
        cost=findViewById(R.id.textView4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Product product=new Product(PaymentActivity.this);
        String productListString=product.getIncomeProduct();
        if (productListString!=null){
            startActivityForResult(
                    new Intent(PaymentActivity.this,AddPaymentActivity.class)
                            .putExtra("list",productListString)
                            .putExtra("title","Добавление товара"),
                    12);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            if(resultCode == Activity.RESULT_OK){

                int id=list.size()+1;
                list.add(new TovarClass(
                        id+"",
                        "",
                        data.getStringExtra("name"),
                        data.getDoubleExtra("finalPriceOne",0),
                        data.getDoubleExtra("cost",0),
                        "КГ",
                        "",
                        data.getDoubleExtra("mass",0)
                ));
                adapter.notifyDataSetChanged();
                updatePrice();
            }

        }
    }
    private class LIDADAPTER extends ArrayAdapter<Product> {


        public LIDADAPTER(Context context) {
            super(context, R.layout.item_payment, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Product cat = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_payment, null);
            }
            ((TextView) convertView.findViewById(R.id.textView13)).setText( cat.name);
            ((TextView) convertView.findViewById(R.id.textView14)).setText( cat.mass+" КГ. "+ cat.costUnit+" руб./кг");
            ((TextView) convertView.findViewById(R.id.textView15)).setText( cat.getCost()+" руб.");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setMessage("Удалить позицию ");
                    builder.setMessage("Удалить позицию "+cat.name+"?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    tovarList.remove(cat);
                                    adapter.notifyDataSetChanged();
                                    updatePrice();
                                }
                            })
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create();
                    builder.show();

                }
            });


            return convertView;
        }

    }
    void updatePrice(){
        double costUser=0;
        for (int i=0;i<tovarList.size();i++){
            costUser+=tovarList.get(i).getCost();
        }
        double costDriver=costUser*0.1;
        String text="Бонус клиенту "+costUser+" руб. \nБонус водителя "+Math.round(costDriver)+" руб.";
        cost.setText(text);
    }
}