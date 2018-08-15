package com.example.david.myparker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class About extends AppCompatActivity {

    String ExternalFontPath;
    Typeface FontLoaderTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(About.this);

        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(getString(R.string.about));
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        ExternalFontPath = "product_sans.ttf";
        FontLoaderTypeface = Typeface.createFromAsset(getAssets(), ExternalFontPath);
        textview.setTypeface(FontLoaderTypeface);
        actionbar.setCustomView(textview);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

    }

    public void endUserAgreement(View view)
    {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.myparker.com/end_user_agreement"));
        startActivity(mIntent);
    }
    public void privacyStatement(View view)
    {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.myparker.com/privacy_statement"));
        startActivity(mIntent);
    }
    public void thirdPartyLicences(View view)
    {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.myparker.com/licences"));
        startActivity(mIntent);
    }
    public void aboutMyParkerDevelopers(View view)
    {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.myparker.com/developers"));
        startActivity(mIntent);
    }
}
