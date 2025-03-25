package com.creditscore.sujeet.samplecreditscore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.creditscore.sujeet.samplecreditscore.R;

import static com.creditscore.sujeet.samplecreditscore.StaticInfo.DEFAULT_SCORE;
import static com.creditscore.sujeet.samplecreditscore.StaticInfo.PREFERENCES;

public class Benifit extends AppCompatActivity {

    Spinner range;
    TextView benefits;
    SharedPreferences sharedPreferences;

    String[] values =
            {
                    "1. Sorry, No benefits available. Improve your credit score.",
                    "1. Loans may or may not be sanctioned.\n\n\n 2. No credit card facilities.\n\n\n 3. Insurance plans with background checking.",
                    "1. May need introducer and collateral for loan approvals.\n\n\n 2. Credit card offers with a limit of 2 lakhs on approvals and background checking.\n\n\n 3. Might qualify for loans on 8-10%\n\n\n"
                            + "4. Insurance plans with background checking.",
                    "1. May need introducer for loan approvals.\n\n\n 2. Credit card offers with a limit of 4 lakhs.\n\n\n 3. Might qaulify for loans on 4-5% interest rate."
                            + "4. Insurance plans with maturity period of over 8 years.",
                    "1. Faster Loan Approvals.\n\n\n 2. Better Credit card offers.\n\n\n 3. Many employers check credit checks as part of employment process.\n\n\n" +
                            "4. Might qualify for loans on 0% interest.\n\n\n 5. Best Insurance premiums."
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benifit);
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        range = (Spinner) findViewById(R.id.spinner);
        benefits = (TextView) findViewById(R.id.text_benefits);
        ArrayAdapter rangeAdapter = ArrayAdapter.createFromResource(this, R.array.ranges, R.layout.support_simple_spinner_dropdown_item);
        rangeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        range.setAdapter(rangeAdapter);
        benefits.setText(getStringFromCreditRange(range.getSelectedItem().toString()));

        range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                benefits.setText(getStringFromCreditRange(range.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public String getStringFromCreditRange(String number) {
        int i = checkRange(number);

        if (i < 400)
            return values[0];
        else if (i < 500)
            return values[1];
        else if (i < 600)
            return values[2];
        else if (i < 700)
            return values[3];
        else
            return values[4];
    }

    public int checkRange(String range) {
        if (range.equals("Current"))
            return 300;
//            return Integer.valueOf(sharedPreferences.getString("score", DEFAULT_SCORE));
        else if (range.equals("300-400"))
            return 320;
        else if (range.equals("400-500"))
            return 420;
        else if (range.equals("500-600"))
            return 520;
        else if (range.equals("600-700"))
            return 620;
        else
            return 720;
    }
}
