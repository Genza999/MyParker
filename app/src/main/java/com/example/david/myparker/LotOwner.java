package com.example.david.myparker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LotOwner extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    String ExternalFontPath;
    Typeface FontLoaderTypeface;

    private GoogleMap mMap;
    private String provider;
    private TextView latitude;
    private TextView longitude;
    private ImageView Imagemarker;
    private LinearLayout addparklot;
    private Button submit;
    Marker mCurrLocationMarker;
    private Button cancel;
    private LatLng mCenterLatLong;
    private TextView mLocationAddress;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_owner);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(LotOwner.this);

        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(getString(R.string.app_name));
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        ExternalFontPath = "product_sans.ttf";
        FontLoaderTypeface = Typeface.createFromAsset(getAssets(), ExternalFontPath);
        textview.setTypeface(FontLoaderTypeface);
        actionbar.setCustomView(textview);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.latitude = findViewById(R.id.lat);
        this.longitude = findViewById(R.id.lon);
        this.Imagemarker = findViewById(R.id.imageMarker1);
        this.addparklot = findViewById(R.id.addparklot1);
        this.submit = findViewById(R.id.submit1);
        this.cancel = findViewById(R.id.cancel1);
        this.mLocationAddress = findViewById(R.id.address);

        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final MapWrapperLayout mapWrapperLayout = findViewById(R.id.map_relative_layout1);
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        // Add a marker in Sydney and move the camera
        if (mMap != null) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            final Location myLocation = locationManager.getLastKnownLocation(provider);
            if (myLocation != null) {
                onLocationChanged(myLocation);
            }

        }
    }

    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.setPosition(latLng);
        } else {
            mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
//        Toast.makeText(this, "We couldn't find any parking nearby", Toast.LENGTH_SHORT).show();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mMap == null) {
            Toast.makeText(this, "map error",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addLot:
                addParkingLot();
                return true;

            case R.id.go_to_login:
                startActivity(new Intent(LotOwner.this, SignInAdminActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addParkingLot() {

        Imagemarker.setVisibility(View.VISIBLE);
        addparklot.setVisibility(View.VISIBLE);
        latitude.setVisibility(View.VISIBLE);
        longitude.setVisibility(View.VISIBLE);
        mLocationAddress.setVisibility(View.VISIBLE);
        mCenterLatLong = mMap.getCameraPosition().target;
        Location mLocation = new Location("");
        mLocation.setLatitude(mCenterLatLong.latitude);
        mLocation.setLongitude(mCenterLatLong.longitude);
        latitude.setText(String.valueOf(mCenterLatLong.latitude));
        longitude.setText(String.valueOf(mCenterLatLong.longitude));
        startIntentService(mLocation);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = String.valueOf(mCenterLatLong.latitude);
                String lon = String.valueOf(mCenterLatLong.longitude);
                String add = mLocationAddress.getText().toString();
                Intent myintent = new Intent(view.getContext(), SignUpAdminActivity.class);
                myintent.putExtra("mytext", lat);
                myintent.putExtra("mytext1", lon);
                myintent.putExtra("mytext2", add);
                startActivity(myintent);
                finish();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                onMapReady(mMap);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addparklot.setVisibility(View.INVISIBLE);
                Imagemarker.setVisibility(View.INVISIBLE);
                mLocationAddress.setVisibility(View.INVISIBLE);
                latitude.setVisibility(View.INVISIBLE);
                longitude.setVisibility(View.INVISIBLE);
            }
        });


    }

    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
//                Toast.makeText(MapsActivity.this,getString(R.string.address_found), Toast.LENGTH_LONG).show();


            }


        }

    }

    protected void displayAddressOutput() {
//          mLocationAddressTextView.setText(mAddressOutput);
//        mLocationAddress.setText(mAreaOutput);
        try {
            if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");
                mLocationAddress.setText(mAreaOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}










