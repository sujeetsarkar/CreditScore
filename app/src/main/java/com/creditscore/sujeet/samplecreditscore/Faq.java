package com.creditscore.sujeet.samplecreditscore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.creditscore.sujeet.samplecreditscore.R;
import com.creditscore.sujeet.samplecreditscore.Animate;

public class Faq extends AppCompatActivity {



    LinearLayout content1, content2, content3, content4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        content1 = (LinearLayout) findViewById(R.id.content1);
        content2 = (LinearLayout) findViewById(R.id.content2);
        content3 = (LinearLayout) findViewById(R.id.content3);
        content4 = (LinearLayout) findViewById(R.id.content4);

        content1.setVisibility(View.GONE);
        content2.setVisibility(View.GONE);
        content3.setVisibility(View.GONE);
        content4.setVisibility(View.GONE);

    }

    public void toggle_contents1(View v){ toggle(content1); }
    public void toggle_contents2(View v){ toggle(content2); }
    public void toggle_contents3(View v){ toggle(content3); }
    public void toggle_contents4(View v){ toggle(content4); }

    public void toggle(LinearLayout content)
    {
        if(content.isShown())
        {
            Animate.slide_up(this, content);
            content.setVisibility(View.GONE);
        }
        else
        {
            content.setVisibility(View.VISIBLE);
            Animate.slide_down(this, content);
        }
    }
}
