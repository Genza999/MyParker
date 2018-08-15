package com.example.david.myparker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignInDriverActivity extends AppCompatActivity {

    private static final String TAG = "SignInDriverActivity";
    EditText password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String admin_data = preferences.getString("admin_data","");
        Boolean isDriverLoggedIn = preferences.getBoolean("isDriverLoggedIn",false);
        if (isDriverLoggedIn){
            Intent intent =new Intent(this, MainActivity.class);
            intent.putExtra("response",admin_data);
            startActivity(intent);
            finish();
            return;
        }

        checkAllPermissions();

        setContentView(R.layout.activity_sign_in_driver);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }




    private static int PERMISSION_ALL = 2123;


    public void checkAllPermissions() {
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void btnDriverSignIn(View view) {
        final String dataPassword = password.getText().toString();
        final String dataEmail = email.getText().toString();

        if (!dataEmail.isEmpty() && !dataPassword.isEmpty()) {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_DRIVER_SIGN_IN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.trim().equals("[]")) {
                        Intent intent =new Intent(SignInDriverActivity.this, MainActivity.class);
                        intent.putExtra("response",response);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInDriverActivity.this, "Sign in failed ...", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignInDriverActivity.this, "Server Connection Failed ...", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", dataEmail);
                    data.put("password", dataPassword);
                    return data;
                }
            });

        } else {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonDriverCreateAccount(View view) {

        startActivity(new Intent(this, SignUpDriverActivity.class));
    }
    public void areYouAParkingLotOwner(View view) {
        Intent Intent = new Intent(this, SignInAdminActivity.class);
        startActivity(Intent);
    }
}
