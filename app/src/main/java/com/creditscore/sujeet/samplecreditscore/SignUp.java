package com.creditscore.sujeet.samplecreditscore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.creditscore.sujeet.samplecreditscore.Login.JSON_ERROR;
import static com.creditscore.sujeet.samplecreditscore.Login.RESPONSE_ERROR;
import static com.creditscore.sujeet.samplecreditscore.StaticInfo.URL_SIGNUP;

public class SignUp extends AppCompatActivity {
    EditText username, panNo, aadharNo;
    SharedPreferences sharedPreferences;
    public static final String PREFERENCES = "login_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        username = (EditText) findViewById(R.id.username_signup);
        panNo = (EditText) findViewById(R.id.panno_signup);
        aadharNo = (EditText) findViewById(R.id.aadhar_signup);
    }
    public void submit(View v)
    {
        if(username.getText().toString().isEmpty())
            Toast.makeText(getBaseContext(), "Enter Username", Toast.LENGTH_SHORT).show();
        else if(panNo.getText().toString().isEmpty())
            Toast.makeText(getBaseContext(), "Enter Pan No", Toast.LENGTH_SHORT).show();
        else if(aadharNo.getText().toString().isEmpty())
            Toast.makeText(getBaseContext(), "Enter Aadhar No", Toast.LENGTH_SHORT).show();
        else if(panNo.getText().toString().length() != 10)
            Toast.makeText(getBaseContext(), "Incorrect Pan No", Toast.LENGTH_SHORT).show();
        else if(aadharNo.getText().toString().length() != 12)
            Toast.makeText(getBaseContext(), "Incorrect Aadhar No", Toast.LENGTH_SHORT).show();
        else
            populateJson();
    }

    public void populateJson()
    {
        String FINAL_URL = URL_SIGNUP + "?uname=" + username.getText().toString() + "&pno="
                + panNo.getText().toString() + "&ano=" + aadharNo.getText().toString();
        Log.i("sjbit_project", FINAL_URL);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, FINAL_URL  , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            if(response.getString("success").equals("1"))
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("is_logged_in", true);
                                editor.putString("username", username.getText().toString());
                                editor.putString("email", response.getString("email"));
                                editor.apply();

                                Intent intent = new Intent(SignUp.this, OtpVerification.class);
                                intent.putExtra("mobile", response.getString("mobile"));
                                intent.putExtra("email", response.getString("email"));
                                startActivity(intent);
                                finish();

                            }
                            else
                                Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();


                        } catch (JSONException error)
                        {
                            Toast.makeText(getBaseContext(), JSON_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), RESPONSE_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }
}
