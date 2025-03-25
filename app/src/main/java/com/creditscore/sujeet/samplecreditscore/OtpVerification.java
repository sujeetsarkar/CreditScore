package com.creditscore.sujeet.samplecreditscore;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

public class OtpVerification extends AppCompatActivity {
    TextView sentSms, emailVerify;
    String SENT_SMS = "The message has been sent to ";
    String EMAIL_VERFIY = "After verifying your email.. default password will be sent to your mail id: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        Bundle extras = getIntent().getExtras();
        SENT_SMS += extras.getString("mobile");
        EMAIL_VERFIY += extras.getString("email");

        sentSms = (TextView) findViewById(R.id.sentsms_otp);
        emailVerify = (TextView) findViewById(R.id.email_verify_otp);
        sentSms.setText(SENT_SMS);
        emailVerify.setText(EMAIL_VERFIY);

    }
    public void verify(View v)
    {
        Toast.makeText(getBaseContext(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LiveCreditScore.class);
        startActivity(intent);
        finish();
    }
}
