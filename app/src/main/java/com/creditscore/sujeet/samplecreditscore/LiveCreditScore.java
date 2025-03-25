package com.creditscore.sujeet.samplecreditscore;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.creditscore.sujeet.samplecreditscore.StaticInfo.DEFAULT_EMAIL;
import static com.creditscore.sujeet.samplecreditscore.StaticInfo.DEFAULT_USERNAME;
import static com.creditscore.sujeet.samplecreditscore.StaticInfo.JSON_ERROR;
import static com.creditscore.sujeet.samplecreditscore.StaticInfo.PREFERENCES;
import static com.creditscore.sujeet.samplecreditscore.StaticInfo.URL_CREDIT_SCORE;

public class LiveCreditScore extends AppCompatActivity {

    Toolbar toolbar;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private IProfile profile;
    private static final int ID_LIVE_CREDIT_SCORE = 0, ID_LOG_OUT = 1, ID_EXIT = 2, ID_BENEFITS = 3,
            ID_NOTIFICATION = 4, ID_PREDICTION = 5, ID_PROFILE = 6, ID_RENTAL_PAYMENT = 7,
            ID_FAQ = 8;
    SharedPreferences sharedPreferences;

    TextView creditScore;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<com.creditscore.sujeet.dto.LiveCreditScoreDTO> listofDTO = new ArrayList<>();
    com.creditscore.sujeet.dto.LiveCreditScoreDTO singleDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_credit_score);
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarInit(savedInstanceState);
        recyclerViewInit();
        creditScore = (TextView) findViewById(R.id.credit_score_live);
        PopulateJson();
    }

    public void refresh(View v)
    {
        PopulateJson();
    }

    public void recyclerViewInit()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new com.creditscore.sujeet.adapter.LiveCreditScoreAdaptor(listofDTO);
        mRecyclerView.setAdapter(mAdapter);

//        for(int i=0; i< 10; i++)
//        {
//            singleDTO = new LiveCreditScoreDTO();
//            singleDTO.setName(String.valueOf(i));
//            listofDTO.add(singleDTO);
//        }
//        mAdapter.notifyDataSetChanged();
    }



    public void PopulateJson() {

        String username = sharedPreferences.getString("username", "");
        String FINAL_URL = URL_CREDIT_SCORE + "?uname=" + username;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, FINAL_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("score", response.getString("credit_score"));
                            editor.commit();

                            creditScore.setText(response.getString("credit_score"));

//                            JSONArray newsfeed = response.getJSONArray("newsfeed");
//                            for(int i=0; i<newsfeed.length(); i++)
//                            {
//                                singleDTO = new com.creditscore.sujeet.dto.LiveCreditScoreDTO();
//                                singleDTO.setName(newsfeed.get(i).toString());
//
//                                listofDTO.add(singleDTO);
//                            }
//                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException error) {
                            Toast.makeText(getBaseContext(), JSON_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    @Override
    public void onBackPressed() {
        if(!result.isDrawerOpen())
            result.openDrawer();
        else
            super.onBackPressed();
    }

    private void toolbarInit(Bundle savedInstanceState)
    {
        profile = new ProfileDrawerItem().withName(sharedPreferences.getString("username", DEFAULT_USERNAME))
                .withEmail(sharedPreferences.getString("email", DEFAULT_EMAIL)).withIcon(R.drawable.user).withIdentifier(ID_PROFILE);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg)
                .withCompactStyle(false)
                .addProfiles(
                        profile,
                        new ProfileSettingDrawerItem().withName("Log Out").withIcon(R.drawable.logout).withIdentifier(ID_LOG_OUT)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if(profile.getIdentifier() == ID_LOG_OUT)
                        {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("is_logged_in", false);
                            editor.apply();
                            Intent intent = new Intent(LiveCreditScore.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(profile.getIdentifier() == ID_PROFILE)
                        {

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("LIVE Credit Score").withIcon(R.drawable.credit).withIdentifier(ID_LIVE_CREDIT_SCORE),
                        new PrimaryDrawerItem().withName("Prediction").withIcon(R.drawable.pred).withIdentifier(ID_PREDICTION),
                        new PrimaryDrawerItem().withName("Benefits").withIcon(R.drawable.benefit).withIdentifier(ID_BENEFITS),
                        new SectionDrawerItem().withName("Services & Awareness"),
                        new PrimaryDrawerItem().withName("Rental Payment").withIcon(R.drawable.pay).withIdentifier(ID_RENTAL_PAYMENT),
                        new SecondaryDrawerItem().withName("Notification").withIcon(R.drawable.notifications).withIdentifier(ID_NOTIFICATION),
                        new SecondaryDrawerItem().withName("FAQs").withIcon(R.drawable.faqq).withIdentifier(ID_FAQ)
                )
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Exit").withIcon(R.drawable.exit).withIdentifier(ID_EXIT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == ID_EXIT)
                        {
                            finish();
                        }
                        else if(drawerItem.getIdentifier() == ID_LIVE_CREDIT_SCORE)
                        {
                            //do nothing
                        }
                        else if(drawerItem.getIdentifier() == ID_PREDICTION)
                        {
                            Intent intent = new Intent(LiveCreditScore.this, Prediction.class);
                            startActivity(intent);
                        }
                        else if(drawerItem.getIdentifier() == ID_BENEFITS)
                        {
                            Intent intent = new Intent(LiveCreditScore.this, Benifit.class);
                            startActivity(intent);
                        }
                        else if(drawerItem.getIdentifier() == ID_NOTIFICATION)
                        {
                            Intent intent = new Intent(LiveCreditScore.this, Notification.class);
                            startActivity(intent);
                        }
                        else if(drawerItem.getIdentifier() == ID_RENTAL_PAYMENT)
                        {
                            Intent intent = new Intent(LiveCreditScore.this, Rental.class);
                            startActivity(intent);
                        }
                        else if(drawerItem.getIdentifier() == ID_FAQ)
                        {
                            Intent intent = new Intent(LiveCreditScore.this, Faq.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }
}
