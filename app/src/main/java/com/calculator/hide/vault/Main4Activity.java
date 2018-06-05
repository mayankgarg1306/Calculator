package com.calculator.hide.vault;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main4Activity extends AppCompatActivity {


    @Override
    public void onPause() {
        if (isApplicationSentToBackground(this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }else{
                ActivityCompat.finishAffinity(this);
            }
        }
        super.onPause();
    }
    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


        private Activity activity;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main4);

            final EditText your_name        = (EditText) findViewById(R.id.your_name);
            final EditText your_email       = (EditText) findViewById(R.id.your_email);
            final EditText your_subject     = (EditText) findViewById(R.id.your_subject);
            final EditText your_message     = (EditText) findViewById(R.id.your_message);



            Button email = (Button) findViewById(R.id.post_message);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name      = your_name.getText().toString();
                    String email     = your_email.getText().toString();
                    String subject   = your_subject.getText().toString();
                    String message   = your_message.getText().toString();
                    if (TextUtils.isEmpty(name)){
                        your_name.setError("Enter Your Name");
                        your_name.requestFocus();
                        return;
                    }

                    Boolean onError = false;
                    if (!isValidEmail(email)) {
                        onError = true;
                        your_email.setError("Invalid Email");
                        return;
                    }

                    if (TextUtils.isEmpty(subject)){
                        your_subject.setError("Enter Your Subject");
                        your_subject.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(message)){
                        your_message.setError("Enter Your Message");
                        your_message.requestFocus();
                        return;
                    }

                    Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
                    sendEmail.setType("plain/text");
                    sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"care.infinitesolutions@gmail.com"});
                    sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                    sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                            "name:"+name+'\n'+"Email ID:"+email+'\n'+"Message:"+'\n'+message);

            /* Send it off to the Activity-Chooser */
                    startActivity(Intent.createChooser(sendEmail, "Send mail..."));


                }
            });
        }

        // validating email id

        private boolean isValidEmail(String email) {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }


    }