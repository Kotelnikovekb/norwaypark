package ru.ekbapp.norwaypark.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.ekbapp.norwaypark.Module.JSONParser;

import static ru.ekbapp.norwaypark.LoginActivity.OLL_PRODUCT_J;
import static ru.ekbapp.norwaypark.LoginActivity.SESSION_S;
import static ru.ekbapp.norwaypark.LoginActivity.SETTINGS_S;

public class Product {

    public String Type;
    public String Category;
    public String Name;
    public String Cost;
    private Context context;
    SharedPreferences sharedPreferences;
    JSONParser jsonParser =null;
    String TAG_S="PRODUCT_CLASS";

    public Product(String type, String category, String name, String cost) {
        Type = type;
        Category = category;
        Name = name;
        Cost = cost;
    }
    public Product(Context context){
        this.context=context;
        sharedPreferences =context.getSharedPreferences(SETTINGS_S,Context.MODE_PRIVATE);
        jsonParser= new JSONParser();
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
    public void updateProductList(){
        if (context!=null){
            try {
                JSONObject data= new AttemptRequest("getProductList",sharedPreferences.getString(SESSION_S,"")).execute().get();
                if (data.getString("susses").equals("1")){
                    JSONArray productList=data.getJSONArray("productList");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString(OLL_PRODUCT_J,productList.toString());
                    editor.commit();
                }else {
                    Log.e(TAG_S,data.getString("message"));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.e(TAG_S,e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG_S,e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG_S,e.toString());
            }
        }

    }
    public String getIncomeProduct(){
        if (context!=null){
            try {
                JSONObject data= new AttemptRequest("getProductList",sharedPreferences.getString(SESSION_S,""),"income").execute().get();
                Log.e(TAG_S+"1",data.toString());
                if (data.getString("susses").equals("1")){
                    JSONArray productList=data.getJSONArray("productList");
                    return productList.toString();
                }else {
                    Log.e(TAG_S,data.getString("message"));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.e(TAG_S,e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG_S,e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG_S,e.toString());
            }
        }
        return null;
    }
    class AttemptRequest extends AsyncTask<String, String, JSONObject> {
        public final String Action;
        public final String Session;
        public String Type;

        public AttemptRequest(String action, String session) {
            Action = action;
            Session = session;
        }

        public AttemptRequest(String action, String session, String type) {
            Action = action;
            Session = session;
            Type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("session", Session));
            params.add(new BasicNameValuePair("action", Action));



            JSONObject json = jsonParser.makeHttpRequest("https://ekb-app.ru/norwaypark/vendor/Product.php",
                    "POST", params);

            return json;

        }

    }
}
