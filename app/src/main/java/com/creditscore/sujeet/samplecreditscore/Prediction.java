package com.creditscore.sujeet.samplecreditscore;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.WindowManager;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.github.mikephil.charting.charts.LineChart;
        import com.github.mikephil.charting.components.LimitLine;
        import com.github.mikephil.charting.components.XAxis;
        import com.github.mikephil.charting.components.YAxis;
        import com.github.mikephil.charting.data.Entry;

        import com.github.mikephil.charting.data.LineData;
        import com.github.mikephil.charting.data.LineDataSet;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.Random;

        import com.creditscore.sujeet.samplecreditscore.AppController;
        import com.creditscore.sujeet.samplecreditscore.R;



        import com.github.mikephil.charting.charts.LineChart;
        import java.util.ArrayList;

        import static com.creditscore.sujeet.samplecreditscore.StaticInfo.URL_PREDICTION;

public class Prediction extends AppCompatActivity {
    public final static String DEFAULT_USERNAME = "guest";
    public final static String JSON_ERROR = "Error! Contact Admin";
    public final static String PREFERENCES = "login_pref";
    public final static String RESPONSE_ERROR = "Please Check your Internet Connection";
    LineChart mChart;
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Entry> yVals = new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_prediction);
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setViewPortOffsets(0,0,0,0);
        mChart.setBackgroundColor(Color.GREEN);

        mChart.setDescription("ENVESNET");
//        mChart.setDescriptionTextSize(45f);

        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        XAxis x = mChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        x.setTextSize(11f);
        x.setTextColor(Color.WHITE);

        LimitLine ll1 = new LimitLine(850f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(300f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(10f);

        LimitLine ll3 = new LimitLine(500f, "Safety");
        ll3.setLineWidth(4f);
        ll3.enableDashedLine(10f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(10f);

        YAxis y = mChart.getAxisLeft();
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.addLimitLine(ll1);
        y.addLimitLine(ll2);
        y.addLimitLine(ll3);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(true);
        y.setAxisLineColor(Color.WHITE);

        mChart.getAxisRight().setEnabled(true);

        PopulateJson();
        x.setValues(xVals);

        mChart.getLegend().setEnabled(true);
        mChart.animateXY(2000,2000);

        mChart.invalidate();
    }


    public void PopulateJson() {


        String FINAL_URL = URL_PREDICTION + "?uname=" + sharedPreferences.getString("username", DEFAULT_USERNAME) ;
        Log.i("hackd", FINAL_URL);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, FINAL_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray xAxis = response.getJSONArray("x-axis");
                            JSONArray yAxis = response.getJSONArray("y-axis");

                            for(int i=0; i<xAxis.length(); i++)
                            {
                                xVals.add(xAxis.get(i).toString());
                                yVals.add(new Entry(Integer.valueOf(yAxis.get(i).toString()), i));
                            }
                            setData();

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


    private void setData()
    {

        LineDataSet set1;

        if(mChart.getData() != null && mChart.getData().getDataSetCount() > 0)
        {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else
        {
            set1 = new LineDataSet(yVals, "Credit Score");
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(true);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(14f);
            set1.setCircleColor(Color.BLACK);
            set1.setHighLightColor(Color.BLUE);
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.MAGENTA);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(true);


            LineData data = new LineData(xVals, set1);
            data.setValueTextSize(9f);
            data.setDrawValues(true);

            mChart.setData(data);
        }
    }
}
