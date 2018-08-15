package com.example.david.myparker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class Help extends AppCompatActivity {


    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            }
        });


        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    }

    public void how_to(View view)
    {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.myparker.com/faq"));
        startActivity(mIntent);
    }

    public void call_us(View view){
        Intent call_intent = new Intent(Intent.ACTION_DIAL);
        call_intent.setData(Uri.parse("tel:0775788332"));
        startActivity(call_intent);
    }

    public void email_us(View view)
    {
        Log.i("Send email", "");

        String [] to ={"bbengoreagan@gmail.com"};
        Intent myInten = new Intent(Intent.ACTION_SEND);
        myInten.setData(Uri.parse("mail to:"));
        myInten.setType("text/plain");
        myInten.putExtra(Intent.EXTRA_EMAIL, to);
        try
        {
            startActivity(Intent.createChooser(myInten, "Send mail......."));
            finish();
            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(Help.this ,"No email client found on device",Toast.LENGTH_LONG).show();
        }
    }

    public void about_us(View view)
    {
        Intent aIntent = new Intent(Help.this, About.class);
        startActivity(aIntent);
    }
}
