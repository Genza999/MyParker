package com.example.david.myparker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsCustom extends AppCompatActivity {

    String ExternalFontPath;
    Typeface FontLoaderTypeface;

    private TextView logout;
    private TextView profile;
    private TextView owner;
    private boolean closeState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_custom);

        this.profile = findViewById(R.id.profile);
        this.owner = findViewById(R.id.owner);
        this.logout = findViewById(R.id.log_out);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(SettingsCustom.this);

        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(getString(R.string.settings));
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        ExternalFontPath = "product_sans.ttf";
        FontLoaderTypeface = Typeface.createFromAsset(getAssets(), ExternalFontPath);
        textview.setTypeface(FontLoaderTypeface);
        actionbar.setCustomView(textview);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsCustom.this, Profile.class));
            }
        });

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsCustom.this, SignInAdminActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverLogout(view);
            }
        });
    }

    public void driverLogout(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isDriverLoggedIn",false);
        editor.apply();

        closeState = true;
        Intent i = new Intent();
        i.putExtra("isClosed",closeState);
        setResult(1234,i);
        finish();

        startActivity(new Intent(this,SignInDriverActivity.class));

    }

    @Override
    public void finish() {
        super.finish();
    }
}


