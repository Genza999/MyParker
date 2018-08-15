package com.example.david.myparker;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.david.myparker.MainActivity.responseWithDriverDetails;

public class Profile extends AppCompatActivity {

    String ExternalFontPath;
    Typeface FontLoaderTypeface;

    TextView firstname;
    TextView contact;
    EditText platenumber;
    Button editText;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.firstname = findViewById(R.id.firstname);
        this.contact = findViewById(R.id.contact);
        this.platenumber = findViewById(R.id.Noplate);
        this.done = findViewById(R.id.done);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(Profile.this);

        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(getString(R.string.user_profile));
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        ExternalFontPath = "product_sans.ttf";
        FontLoaderTypeface = Typeface.createFromAsset(getAssets(), ExternalFontPath);
        textview.setTypeface(FontLoaderTypeface);
        actionbar.setCustomView(textview);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

        try {
            JSONArray jsonArray = new JSONArray(responseWithDriverDetails);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            firstname.setText(jsonObject.getString("name"));
            platenumber.setText(jsonObject.getString("car_number_plate"));
            contact.setText(jsonObject.getString("phone_number"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstname.setInputType(InputType.TYPE_NULL);
                contact.setInputType(InputType.TYPE_NULL);
                platenumber.setInputType(InputType.TYPE_NULL);

            }
        });

    }
}
