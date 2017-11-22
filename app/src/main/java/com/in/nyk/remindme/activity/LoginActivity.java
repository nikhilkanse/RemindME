package com.in.nyk.remindme.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.in.nyk.remindme.R;

/**
 * Created by nikhilkanse on 21/11/17.
 */

public class LoginActivity extends Activity {

    //Mark :- Class variables.
    private TextView messageTextView;
    private SignInButton googleSignInButton;
    private Button continueButton;

    //Mark :- Override methods.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        messageTextView = (TextView) findViewById(R.id.message_Textview);
        googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        continueButton = (Button) findViewById(R.id.continue_button);

        setFont();
        setGooglePlusButtonText(getResources().getString(R.string.google_login_button_msg_string));

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainScreen();
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGoogleLogin();
            }
        });

    }


    //Mark :- Class Private methods.
    private void handleGoogleLogin() {

    }

    private void launchMainScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
    }

    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.ttf_file_path));
        messageTextView.setTypeface(custom_font);
    }

    protected void setGooglePlusButtonText(String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
}
