package com.creditscore.sujeet.samplecreditscore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.creditscore.sujeet.samplecreditscore.AppController;

import static com.creditscore.sujeet.samplecreditscore.StaticInfo.URL_LOGIN;

public class Login extends AppCompatActivity {
    EditText username, password;
    SharedPreferences sharedPreferences;
    boolean isLoginMatched = false;
    public static final String PREFERENCES = "login_pref";

    public final static String JSON_ERROR = "Error! Contact Admin";
    public final static String RESPONSE_ERROR = "Please Check your Internet Connection";
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.pass_login);

        if(sharedPreferences.getBoolean("is_logged_in", false))
        {
            Intent intent = new Intent(this, LiveCreditScore.class);
            startActivity(intent);
            finish();
        }
    }
    public void login(View v)
    {
        if(username.getText().toString().isEmpty())
            Toast.makeText(getBaseContext(), "Enter Username", Toast.LENGTH_SHORT).show();
        else if(password.getText().toString().isEmpty())
            Toast.makeText(getBaseContext(), "Enter Password", Toast.LENGTH_SHORT).show();
        else
            PopulateJson();
    }
    public void sign_up(View v)
    {
        Intent i=new Intent(this,SignUp.class);
        startActivity(i);
        finish();
    }
    public void forgot(View v)
    {

    }

    public void PopulateJson() {
        final String userString = username.getText().toString();
        String passString = password.getText().toString();
        String FINAL_URL = URL_LOGIN + "?uname=" + userString + "&passwd=" + passString;
        Log.i("sjbit_project", FINAL_URL);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, FINAL_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("success").equals("1")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("is_logged_in", true);
                                editor.putString("username", userString);
                                editor.putString("email", response.getString("email"));
                                editor.apply();
                                Intent intent = new Intent(Login.this, LiveCreditScore.class);
                                startActivity(intent);
                                finish();
                            }

                            Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();


                        } catch (JSONException error) {
                            Toast.makeText(getBaseContext(), JSON_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), RESPONSE_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }
}
