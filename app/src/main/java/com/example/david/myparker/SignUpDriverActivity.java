package com.example.david.myparker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpDriverActivity extends AppCompatActivity {

    private static final String TAG = "SignUpDriverActivity";
    public static String PATTERN_USERNAME = "[a-zA-Z\\s]+";
    public static String PATTERN_CAR_NUMBER_PLATE = "[a-zA-Z]{3}\\s[0-9]{3}[a-zA-Z]";
    public static String PATTERN_CAR_NUMBER_PLATE_UG = "[Uu][a-zA-Z]{2}\\s[0-9]{3}[a-zA-Z]";
    public static String PATTERN_EMAIL = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
    public static String PATTERN_PASSWORD = ".{6,30}";

    static boolean validateData(String str, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.matches();
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_driver);


        final EditText name = findViewById(R.id.name);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);
        final EditText email = findViewById(R.id.email);
        final EditText carNumberPlate = findViewById(R.id.carNumberPlate);
        final EditText password = findViewById(R.id.password);
        final EditText passwordRetype = findViewById(R.id.retype_password);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dataName = name.getText().toString();
                final String dataEmail = email.getText().toString();
                final String dataCarNumberPlate = carNumberPlate.getText().toString();
                final String dataPhoneNumber = phoneNumber.getText().toString();
                final String dataPassword = password.getText().toString();
                String dataPasswordRetype = passwordRetype.getText().toString();


                if (!validateData(dataName,PATTERN_USERNAME)){
                    Toast.makeText(SignUpDriverActivity.this, "Enter valid UserName ( only characters are allowed)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validEmail(dataEmail)) {
                    Toast.makeText(SignUpDriverActivity.this, "Enter valid e-mail Address!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (dataPhoneNumber.length()!=10){
                    Toast.makeText(SignUpDriverActivity.this, "Enter valid Phone Number (10 numbers)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validateData(dataCarNumberPlate,PATTERN_CAR_NUMBER_PLATE)){
                    Toast.makeText(SignUpDriverActivity.this, "Enter valid Car Number Plate ( XXX 123X)", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!validateData(dataPassword,PATTERN_PASSWORD)){
                    Toast.makeText(SignUpDriverActivity.this, "Enter valid Password (6 - 30 characters)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dataPassword.equals(dataPasswordRetype)) {

                    RequestQueue requestQueue = Volley.newRequestQueue(SignUpDriverActivity.this);
                    requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_DRIVER_REGISTRATION, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "onResponse: " + response);
                            Toast.makeText(SignUpDriverActivity.this, "RESPONSE : " + response, Toast.LENGTH_SHORT).show();
                            if (response.trim().equals("1")) {
                                /*Intent intent = new Intent(SignUpDriverActivity.this, MainActivity.class);
                                startActivity(intent);
*/
                                Toast.makeText(SignUpDriverActivity.this, "Account created ... U can now log in", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SignUpDriverActivity.this, "Failed to save information ...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpDriverActivity.this, "Server Connection Failed ...", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();

                            data.put("name", dataName);
                            data.put("carNumberPlate", dataCarNumberPlate);
                            data.put("email", dataEmail);
                            data.put("phoneNumber", dataPhoneNumber);
                            data.put("phoneNumber", dataPhoneNumber);
                            data.put("password", dataPassword);

                            return data;
                        }
                    });
                }else {
                    Toast.makeText(SignUpDriverActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void goToLogin(View view) {
        Intent Intent = new Intent(this, SignInDriverActivity.class);
        startActivity(Intent);
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_USERNAME_REGEX = Pattern.compile("[a-z]+$");

    public static boolean validate(String str, Pattern regex) {
        Matcher matcher = regex.matcher(str);
        return matcher.find();
    }

    public void areYouAParkingLotOwner(View view) {
        Intent Intent = new Intent(this, LotOwner.class);
        startActivity(Intent);
    }
}
