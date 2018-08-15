package com.example.david.myparker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpAdminActivity extends AppCompatActivity {

    private static final String TAG = "SignUpAdminActivity";
    private ImageView image1;

    Bitmap bitmapInterior;
    Bitmap bitmapExterior;
    ImageView imageExterior;
    ImageView imageInterior;
    View exteriorPick;
    View interiorPick;

    public static String PATTERN_USERNAME = "[a-zA-Z\\s]+";
    public static String PATTERN_CAR_NUMBER_PLATE = "[a-zA-Z]{3}\\s[0-9]{3}[a-zA-Z]";
    public static String PATTERN_CAR_NUMBER_PLATE_UG = "[Uu][a-zA-Z]{2}\\s[0-9]{3}[a-zA-Z]";
    public static String PATTERN_EMAIL = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
    public static String PATTERN_PASSWORD = ".{6,30}";

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    static boolean validateData(String str, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.matches();
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);

        final EditText parkingname = findViewById(R.id.Parkingname);
        final EditText slots = findViewById(R.id.slots);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText passwordRetype = findViewById(R.id.retype_password);
        final EditText phoneNumber = findViewById(R.id.phone_number);

        final TextView latitude = findViewById(R.id.latitude);
        final TextView longitude = findViewById(R.id.longitude);
        final TextView address = findViewById(R.id.location);



        Button send = findViewById(R.id.send1);

        latitude.setText(getIntent().getStringExtra("mytext"));
        longitude.setText(getIntent().getStringExtra("mytext1"));
        address.setText(getIntent().getStringExtra("mytext2"));


        exteriorPick = findViewById(R.id.exterior_pick);
        interiorPick = findViewById(R.id.interior_pick);
        imageExterior = findViewById(R.id.imageExterior);
        imageInterior = findViewById(R.id.imageInterior);

        exteriorPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPictureFromGallery(1);
            }
        });
        interiorPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPictureFromGallery(2);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String dataName = parkingname.getText().toString();
                final String dataEmail = email.getText().toString();
                final String dataPassword = password.getText().toString();
                String dataPasswordRetype = passwordRetype.getText().toString();
                final String dataLatitude = latitude.getText().toString();
                final String dataLongitude = longitude.getText().toString();
                final String dataAddress = address.getText().toString();
                final String dataNumberOfLots = slots.getText().toString();
                final String dataPhoneNumber = phoneNumber.getText().toString();

                if (!validateData(dataName,PATTERN_USERNAME)){
                    Toast.makeText(SignUpAdminActivity.this, "Enter valid UserName ( only characters are allowed)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validEmail(dataEmail)) {
                    Toast.makeText(SignUpAdminActivity.this, "Enter valid e-mail Address!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (dataPhoneNumber.length()!=10){
                    Toast.makeText(SignUpAdminActivity.this, "Enter valid Phone Number (10 numbers)", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (dataAddress.length()==0){
                    Toast.makeText(SignUpAdminActivity.this, "Enter valid Address (Village, City)", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!validateData(dataPassword,PATTERN_PASSWORD)){
                    Toast.makeText(SignUpAdminActivity.this, "Enter valid Password (6 - 30 characters)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (bitmapExterior != null && bitmapInterior != null) {
                    if (dataPassword.equals(dataPasswordRetype)) {
                        RequestQueue requestQueue = Volley.newRequestQueue(SignUpAdminActivity.this);
                        requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_ADMIN_REGISTRATION, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "onResponse: "+response );
                                Toast.makeText(SignUpAdminActivity.this, "RESPONSE : " + response, Toast.LENGTH_SHORT).show();
                                if (response.trim().equals("1")) {
                                    Intent intent =new Intent(SignUpAdminActivity.this, MainActivityParkingLotOwner.class);
                                    startActivity(intent);

                                    Toast.makeText(SignUpAdminActivity.this, "Account created ...", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(SignUpAdminActivity.this, "Failed to save information ...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SignUpAdminActivity.this, "Server Connection Failed ...", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();

                                data.put("imageExterior", getStringFromBitmap(scaleDownBitmap(bitmapExterior)));
                                data.put("imageInterior", getStringFromBitmap(scaleDownBitmap(bitmapInterior)));
                                data.put("name", dataName);
                                data.put("address", dataAddress);
                                data.put("gps_codes", dataLatitude + "," + dataLongitude);
                                data.put("number_of_slots", dataNumberOfLots);
                                data.put("email", dataEmail);
                                data.put("phoneNumber", dataPhoneNumber);
                                data.put("password", dataPassword);

                                return data;
                            }
                        });

                    }else {
                        Toast.makeText(SignUpAdminActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    Toast.makeText(SignUpAdminActivity.this, "Pick Images", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static String getStringFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static Bitmap scaleDownBitmap(Bitmap bitmapOriginal) {
        if (bitmapOriginal.getWidth() > 350) {
            int factor = bitmapOriginal.getHeight() / 350;
            Bitmap bitmapSmall = Bitmap.createScaledBitmap(
                    bitmapOriginal,
                    bitmapOriginal.getWidth() / factor,
                    bitmapOriginal.getHeight() / factor,
                    false);
            return bitmapSmall;
        } else {
            return bitmapOriginal;
        }
    }

    private void pickPictureFromGallery(int imageCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, imageCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case 1:
                        try {
                            bitmapExterior = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                            imageExterior.setImageBitmap(bitmapExterior);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            bitmapInterior = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                            imageInterior.setImageBitmap(bitmapInterior);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }
    public void goToLogin(View view) {
        Intent Intent = new Intent(this, SignInAdminActivity.class);
        startActivity(Intent);
    }
}
