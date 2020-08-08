package ru.ekbapp.norwaypark.Class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.ekbapp.norwaypark.Module.JSONParser;
import ru.ekbapp.norwaypark.R;

import static ru.ekbapp.norwaypark.LoginActivity.SESSION_S;
import static ru.ekbapp.norwaypark.LoginActivity.SETTINGS_S;
import static ru.ekbapp.norwaypark.LoginActivity.USER_NAME_S;

public class User {
    Context context;
    String TAG_S="USER_CLASS";
    public String USER_NAME;
    JSONParser jsonParser = new JSONParser();
    SharedPreferences sharedPreferences;



    public User(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(SETTINGS_S,Context.MODE_PRIVATE);
        USER_NAME=sharedPreferences.getString(USER_NAME_S,"");
    }
    public boolean loginUser(String session){
        try {
            JSONObject data= new AttemptLoginSession().execute(session).get();
            if (data.getString("susses").equals("1")){
                return true;
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
        return false;
    }
    public boolean loginUser(String login,String password){

        try {
            JSONObject data= new AttemptLogin().execute(login,password).get();
            if (data.getString("susses").equals("1")){
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(SESSION_S,data.getString("session"));
                editor.putString(USER_NAME_S,data.getString("user_name"));
                editor.commit();
                return true;
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
        return false;
    }
     class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String password = args[1];
            String login = args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("login", login));
            params.add(new BasicNameValuePair("action", "login"));


            JSONObject json = jsonParser.makeHttpRequest("https://ekb-app.ru/norwaypark/vendor/User.php",
                    "POST", params);

            return json;

        }

    }
    class AttemptLoginSession extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String session = args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("session", session));
            params.add(new BasicNameValuePair("action", "autoLogin"));


            JSONObject json = jsonParser.makeHttpRequest("https://ekb-app.ru/norwaypark/vendor/User.php",
                    "POST", params);

            return json;

        }

    }
}
