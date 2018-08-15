package com.example.david.myparker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter, ResultCallback<Status> {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;

    ArrayList<LatLng> markerPoints;
    Polyline line;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSlots;
    private TextView infoSnippet;
    private Button infoButton;
    private Button infoButton1;
    private Button infoButton2;
    private Button infoButton3;
    private Button infoButton4;
    private Button infoButton5;
    private Button search1;
    private EditText searchView1;
    private OnInfoWindowElemTouchListener infoButtonListener2;
    private OnInfoWindowElemTouchListener infoButtonListener3;
    private OnInfoWindowElemTouchListener infoButtonListener4;
    private LocationRequest mLocationRequest;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F;

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 20;

    private EditText mLocationAddress;
    private TextView mLocationMarkerText;
    private LatLng mCenterLatLong;
    private TextView submit;
    private TextView cancel;
    private LinearLayout addparklot;
    private boolean state;

    ImageView centerMarker;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    FusedLocationProviderClient mFusedLocationClient;
    GeofencingClient client;
    protected ArrayList<Geofence> mGeofenceList;
    private Location lastLocation;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;

    private String provider;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    private AddressResultReceiver mResultReceiver;

    private ParkingLocationStoreDB locationStoreDB;
    private LayoutInflater inflater;
    private Context context;
    public static ImageView parking;
    public static String parkingTitle;
    private TextView confirmremove;
    private TextView cancelremove;
    private LinearLayout layoutView;
    private LinearLayout layoutView3;
    private ImageView lotImage;
    private TextView parkingAt;
    private TextView confirmparking;
    private TextView cancelparking;
    private String address1;

    private NavigationView navView;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private OnInfoWindowElemTouchListener infoButtonListener5;

    // Create a Intent send by the notification
    /*** Receiver registered with this activity to get the response from FetchAddressIntentService.*/

    public static String responseWithDriverDetails = "";
    private String driverId = "";
    private String driverName = "";
    private String driverEmail = "";
    private String driverCarNumberPlate = "";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String isParkedKey = "isParkedKey";

    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView navDrawerButton;

    FloatingActionButton fabPlus, fabWalk, fabSatellite;
    Animation fabOpen, fabClose, fabClockwise, fabAnticlockwise, fabHalfTransparency, fabNoTransparency;
    Animation verticalMovement, mAnimation;
    ImageView openMe;
    boolean isOpen = false;
    MapView mMapView;

//    private ClusterManager<StringClusterItem> mClusterManager;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createGoogleApi();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.infowindow, null);
        this.infoTitle = infoWindow.findViewById(R.id.title);
        this.infoSnippet = infoWindow.findViewById(R.id.snippet);
        this.searchView1 = findViewById(R.id.searchView1);

        this.infoSlots = infoWindow.findViewById(R.id.slot_number);
        this.search1 = findViewById(R.id.search1);
        this.mLocationAddress = findViewById(R.id.Address);
        this.parking = findViewById(R.id.parking);
        centerMarker = findViewById(R.id.imageMarker);
        layoutView = findViewById(R.id.layoutView);
        layoutView3 = findViewById(R.id.layoutView3);
        lotImage = findViewById(R.id.lotImage);
        confirmparking = findViewById(R.id.confirmparking);
        cancelparking = findViewById(R.id.cancelLot);
        parkingAt = findViewById(R.id.parkingAt);
        cancelremove = findViewById(R.id.cancelremove);
        confirmremove = findViewById(R.id.confirmremove);
        this.infoButton4 = infoWindow.findViewById(R.id.button4);
        this.infoButton5 = infoWindow.findViewById(R.id.buttonParkNow);
        openMe = findViewById(R.id.open_me_image);

        fabSatellite = findViewById(R.id.satelliteView);
        fabWalk = findViewById(R.id.walk);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        fabHalfTransparency = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.half_transparency);
        fabNoTransparency = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.no_transparency);
        verticalMovement = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.vertical_movement);



        openMe.startAnimation(verticalMovement);
        openMe.setVisibility(View.VISIBLE);
        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.3f);
        mAnimation.setDuration(5000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());

        openMe.setAnimation(mAnimation);



        markerPoints = new ArrayList<LatLng>();
        this.state = false;
        responseWithDriverDetails = getIntent().getExtras().getString("response");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();


        editor.putString("driver_data", responseWithDriverDetails);
        editor.putBoolean("isDriverLoggedIn", true);
        editor.apply();

        try {
            JSONArray jsonArray = new JSONArray(responseWithDriverDetails);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            driverId = jsonObject.getString("driver_id");
            driverName = jsonObject.getString("name");
            driverEmail = jsonObject.getString("email");
            driverCarNumberPlate = jsonObject.getString("car_number_plate");

            isParkedKey = driverId.trim() + "isParked";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerPoints = new ArrayList<LatLng>();
        this.state = false;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        client = LocationServices.getGeofencingClient(this);

        mGeofenceList = new ArrayList<Geofence>();

        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        isNetworkConnected();

        if (googleApiClient == null) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(MainActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navDrawerButton = findViewById(R.id.navigation_drawer_button);
        navDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///--------------------------------------

        View navHeader = navigationView.getHeaderView(0);
        TextView name = navHeader.findViewById(R.id.dname);
        name.setText(driverName);

        navView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }



    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "product_sans.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void removeParkingRecordInDatabase(final String driverName) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_REMOVE_PARKING_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: "+response );

                editor.putBoolean(isParkedKey, false);
                editor.apply();

                Toast.makeText(MainActivity.this, "RES : " + response, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Car has been removed from parking ...", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Connection to server failed ...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("driverName", driverName);
                return data;
            }
        });

    }

    private void putParkingRecordInDatabase(final String markerTag, final String driverId, final String driverName, final String driverCarNumberPlate) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_NEW_PARKING_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );

                editor.putBoolean(isParkedKey, true);
                editor.apply();

                Toast.makeText(MainActivity.this, "RES : " + response, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Your Car has been Parked ...", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Connection to server failed ...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("driverId", driverId);
                data.put("driverName", driverName);
                data.put("driverCar", driverCarNumberPlate);
                data.put("parkingId", markerTag);
                return data;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void editProfileButton(View view) {
        Intent Intent = new Intent(this, Profile.class);
        startActivity(Intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
            startActivity(new Intent(MainActivity.this, Payment.class));

        } else if (id == R.id.nav_settings) {
            startActivityForResult(new Intent(MainActivity.this, SettingsCustom.class), 1234);

        } else if (id == R.id.nav_help) {
            startActivity(new Intent(MainActivity.this, Help.class));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, About.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    ArrayList<ParkingLot> parkingLotsList = new ArrayList<>();


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        try {
            mMapView = findViewById(R.id.map);
            final ViewGroup parent = (ViewGroup) mMapView.findViewWithTag("GoogleMapMyLocationButton").getParent();


            parent.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Resources r = getResources();
                        //convert our dp margin into pixels
                        int marginPixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
                        // Get the map compass view
                        View mapCompass = parent.getChildAt(4);

                        // create layoutParams, giving it our wanted width and height(important, by default the width is "match parent")
                        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(mapCompass.getHeight(),mapCompass.getHeight());
                        // position on top right
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                        //give compass margin
                        rlp.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
                        mapCompass.setLayoutParams(rlp);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        final List<MarkerOptions> markers = new ArrayList<>();
        final MapWrapperLayout mapWrapperLayout = findViewById(R.id.map_layout);
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        ApplicationService applicationService = new ApplicationService(MainActivity.this);
        final Location newlocation =  applicationService.getLocation(LocationManager.NETWORK_PROVIDER);

        try {
            if(newlocation != null && isNetworkConnected() ) {

                double latitude = newlocation.getLatitude();
                double longitude = newlocation.getLongitude();
                final LatLng latLng = new LatLng(latitude, longitude);
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.setPosition(latLng);
                } else {
                    int height = 135;
                    int width = 70;
                    BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_custom);
                    Bitmap b=bitmapDraw.getBitmap();
                    Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(marker_icon)));

                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                markerPoints.add(latLng); //added
//              redrawLine();


                this.infoButtonListener4 = new OnInfoWindowElemTouchListener(infoButton4,
                        getResources().getDrawable(R.drawable.custom_lightblue_navigation_arrow),
                        getResources().getDrawable(R.drawable.custom_lightblue_navigation_arrow)) {
                    @Override
                    protected void onClickConfirmed(View v, Marker marker) {
                        String url = getDirectionsUrl(latLng, marker.getPosition());

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                        populateGeofence(marker.getPosition().latitude, marker.getPosition().longitude, marker.getTitle());

                        parkingTitle = marker.getTitle();

                        if (!mGoogleApiClient.isConnected()) {
                            Toast.makeText(MainActivity.this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
                        }

                        try {
                            client.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // your success code
                                            Toast.makeText(MainActivity.this, "Geofence created", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // your fail code;
                                            Toast.makeText(MainActivity.this, "Geofence Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } catch (SecurityException securityException) {
                            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
                            Log.d("!ACCESSFINELOCATION", securityException.toString());

                        }

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                mMap.clear();
                                onMapReady(mMap);

                            }
                        });


                    }
                };
                this.infoButton4.setOnTouchListener(infoButtonListener4);

                this.infoButtonListener5 = new OnInfoWindowElemTouchListener(infoButton5,
                        getResources().getDrawable(R.drawable.car_lightblue),
                        getResources().getDrawable(R.drawable.car_lightblue)) {
                    @Override
                    protected void onClickConfirmed(View v, Marker marker) {
                        String markerTag = (String) marker.getTag();
                        if (!preferences.getBoolean(isParkedKey, false)) {
                            Toast.makeText(MainActivity.this, "TAG : " + markerTag, Toast.LENGTH_SHORT).show();
                            putParkingRecordInDatabase(markerTag, driverId, driverName, driverCarNumberPlate);
                        } else {
                            removeParkingRecordInDatabase(driverName);
                        }
                    }
                };
                this.infoButton5.setOnTouchListener(infoButtonListener5);


                final FloatingActionButton fabPlus = findViewById(R.id.fab_plus);
                fabPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isOpen){
                            parking.setVisibility(View.GONE);
                            fabWalk.setVisibility(View.GONE);
                            fabSatellite.setVisibility(View.GONE);
                            parking.startAnimation(fabClose);
                            fabWalk.startAnimation(fabClose);
                            fabSatellite.startAnimation(fabClose);
                            parking.setClickable(false);
                            fabWalk.setClickable(false);
                            fabSatellite.setClickable(false);
                            isOpen = false;
                            fabPlus.setImageResource(R.drawable.arrow_facing_up_dark);
                            fabPlus.setAlpha(255);
                        } else {
                            parking.setVisibility(View.VISIBLE);
                            fabWalk.setVisibility(View.VISIBLE);
                            fabSatellite.setVisibility(View.VISIBLE);
                            parking.startAnimation(fabOpen);
                            fabWalk.startAnimation(fabOpen);
                            fabSatellite.startAnimation(fabOpen);
                            parking.setClickable(true);
                            fabWalk.setClickable(true);
                            fabSatellite.setClickable(true);
                            isOpen = true;
                            fabPlus.setImageResource(R.drawable.arrow_facing_down_dark);
                            fabPlus.setAlpha(64);
                        }
                    }
                });

                final FloatingActionButton satelliteView = findViewById(R.id.satelliteView);
                satelliteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (state == false) {
                            mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                            float zoomLevel = (float) 15.0;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                            state = true;

                            satelliteView.setImageResource(R.drawable.ic_satellite_brown_24dp);
                        } else if (state == true) {
                            mMap.setMapType(mMap.MAP_TYPE_NORMAL);
                            float zoomLevel = (float) 14.0;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                            state = false;

                            satelliteView.setImageResource(R.drawable.ic_satellite_black_24dp);
                        }
                    }
                });

                FloatingActionButton walk = findViewById(R.id.walk);
                walk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParkingLocationStoreDB locationStoreDB = new ParkingLocationStoreDB(getApplicationContext());
                        Cursor cursor = locationStoreDB.getAllLocations();
                        if (cursor.getCount() > 0) {
                            locationStoreDB = new ParkingLocationStoreDB(MainActivity.this);
                            cursor = locationStoreDB.getAllLocations();
                            cursor.moveToFirst();

                            Double parkedLatitude = cursor.getDouble(cursor.getColumnIndex("lat"));
                            Double parkedLongitude = cursor.getDouble(cursor.getColumnIndex("lng"));

                            try {
                                String url1 = getDirectionsUrl1(latLng, parkedLatitude, parkedLongitude);
                                DownloadTask downloadTask1 = new DownloadTask();

                                // Start downloading json data from Google Directions API
                                downloadTask1.execute(url1);
                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "No Internet Connection" + e, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please park your Car", Toast.LENGTH_LONG).show();
                        }

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                mMap.clear();
                                onMapReady(mMap);

                            }
                        });
                    }
                });

                final FloatingActionButton parking = findViewById(R.id.parking);
                parking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (confirmparking.getText().equals(getResources().getString(R.string.accept))) {
                            // Database class is initialised
                            locationStoreDB = new ParkingLocationStoreDB(MainActivity.this);
                            // gets the values from the table
                            Cursor cursor = locationStoreDB.getAllLocations();
                            int length = cursor.getCount();
                            //checking if the user has parked his car or not
                            if (length == 0) {
                                layoutView.setVisibility(View.VISIBLE);
                                // car is not parked. Initialize data accordingly
                                // sets marker on the current location
                                insertCurrentParkingPositionToDB(newlocation.getLatitude(), newlocation.getLongitude(), 14.0f);
                                parkingAt.setText(getAddress(newlocation.getLatitude(), newlocation.getLongitude()));
                                parkingAt.setVisibility(View.GONE);
                                parking.setImageResource(R.drawable.car_location_icon);
                                setMarker1(newlocation.getLatitude(), newlocation.getLongitude(), 14.0f, false);


                            } else if (length == 1) {
                                // car is already parked
                                //shows marker at location stored in DB
                                Toast.makeText(MainActivity.this, "You Already parked your Car", Toast.LENGTH_SHORT).show();

                                // gets the first and only record in the db
                                if (cursor.moveToFirst()) {
                                    Double parkedLatitude = cursor.getDouble(cursor.getColumnIndex("lat"));
                                    Double parkedLongitude = cursor.getDouble(cursor.getColumnIndex("lng"));
                                    String zoom = cursor.getString(cursor.getColumnIndex("zom"));
                                    parkingAt.setVisibility(View.VISIBLE);
                                    parkingAt.setText(getAddress(parkedLatitude, parkedLongitude));
                                    setMarker1(parkedLatitude, parkedLongitude, Float.parseFloat(zoom), true);
                                    confirmparking.setText(getResources().getString(R.string.done));

                                }
                                parking.setImageResource(R.drawable.car_parked_red_location_icon);
                            }
                        } else {

                            // changes marker to the original marker
                            layoutView3.setVisibility(View.VISIBLE);
                            confirmremove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteCurrentParkingRecordFromDB();
                                    confirmparking.setText(getResources().getString(R.string.accept));
                                    parking.setImageResource(R.drawable.car_location_icon);
                                    Toast.makeText(MainActivity.this, "Parked location removed", Toast.LENGTH_SHORT).show();
                                    parkingAt.setVisibility(View.INVISIBLE);
                                    layoutView3.setVisibility(View.INVISIBLE);
                                    mMap.clear();
                                    onMapReady(mMap);
                                }
                            });

                            cancelremove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    layoutView3.setVisibility(View.INVISIBLE);

                                }
                            });
                        }

                        cancelparking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                layoutView.setVisibility(View.INVISIBLE);
                                parkingAt.setVisibility(View.INVISIBLE);
                                parking.setImageResource(R.drawable.car_location_icon);
                                mMap.clear();
                                onMapReady(mMap);
                            }
                        });

                        confirmparking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirmparking.setText(getResources().getString(R.string.done));
                                layoutView.setVisibility(View.INVISIBLE);
                                parkingAt.setVisibility(View.INVISIBLE);
                                onMapReady(mMap);
                            }
                        });
                    }
                });

                FloatingActionButton findparkspot = findViewById(R.id.findparkspot);
                findparkspot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            float mindist = 0;
                            int pos = 0;
                            for (int i = 0; i < markers.size(); i++) {
                                Double slatlng1 = markers.get(i).getPosition().latitude;
                                Double slatlng2 = markers.get(i).getPosition().longitude;
                                LatLng lat = new LatLng(slatlng1, slatlng2);

                                int height = 70;
                                int width = 70;
                                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.myparker_logo_marker);
                                Bitmap b=bitmapdraw.getBitmap();
                                Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

                                mMap.addMarker(new MarkerOptions()
                                        .title(markers.get(i).getTitle())
                                        .snippet(markers.get(i).getSnippet())
                                        .icon(BitmapDescriptorFactory.fromBitmap(marker_icon))
                                        .position(lat)
                                );

                                float[] distance = new float[1];

                                Location.distanceBetween(newlocation.getLatitude(), newlocation.getLongitude(), slatlng1, slatlng2, distance);
                                if (i == 0)
                                    mindist = distance[0];
                                else if (mindist > distance[0]) {
                                    mindist = distance[0];
                                    pos = i;
                                }
                            }

                            Toast.makeText(MainActivity.this, "Closest Parking Spot: " + markers.get(pos).getTitle(), Toast.LENGTH_LONG).show();
                            CameraPosition myPosition = new CameraPosition.Builder()
                                    .target(markers.get(pos).getPosition()).zoom(14).bearing(90).tilt(30).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

                            String url = getDirectionsUrl(latLng, markers.get(pos).getPosition());
                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);

                        } catch (Exception e) {

                        }
                    }
                });

                search1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String g = searchView1.getText().toString();

                        Geocoder geocoder = new Geocoder(getBaseContext());
                        List<Address> addresses;

                        try {
                            // Getting a maximum of 3 Addresses that matches the input text
                            addresses = geocoder.getFromLocationName(g, 3);
                            if (addresses != null && !addresses.equals(""))
                                search(addresses);

                        } catch (Exception e) {

                        }

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                mMap.clear();
                                onMapReady(mMap);
                            }
                        });
                    }
                });
            }

        } catch (Exception e) {

        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_GET_PARKING_LOTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ParkingLot parkingLot = new ParkingLot(
                                jsonObject.getString("parking_lot_id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("address"),
                                jsonObject.getString("description"),
                                jsonObject.getString("gps_codes"),
                                jsonObject.getString("number_of_slots"),
                                jsonObject.getString("email"),
                                jsonObject.getString("phone_number"),
                                jsonObject.getString("password"),
                                jsonObject.getString("photo_external"),
                                jsonObject.getString("photo_internal"),
                                jsonObject.getString("number_of_used_slots")
                        );
                        parkingLotsList.add(parkingLot);

                        int height = 70;
                        int width = 70;
                        BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.myparker_logo_marker);
                        Bitmap b=bitmapDraw.getBitmap();
                        Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

                        infoSlots.setText( parkingLot.number_of_used_slots+"/"+parkingLot.number_of_slots);
                        String[] gps_codes_array = parkingLot.gpscodes.split(",");
                        LatLng loc = new LatLng(Float.valueOf(gps_codes_array[0]), Float.valueOf(gps_codes_array[1]));
                        MarkerOptions gc = new MarkerOptions().position(loc).title(parkingLot.name)
                                .snippet(parkingLot.number_of_used_slots+"/"+parkingLot.number_of_slots)
                                .icon(BitmapDescriptorFactory.fromBitmap(marker_icon));
                        mMap.addMarker(gc).setTag(parkingLot.parking_lot_id);
                        markers.add(gc);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Connection to server failed ...", Toast.LENGTH_SHORT).show();
            }
        }));


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoButtonListener4.setMarker(marker);
                infoButtonListener5.setMarker(marker);


                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
        mMap.setOnInfoWindowClickListener(this);

        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);

        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofencesTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    public void populateGeofence(double lat, double lng, String title) {

        mGeofenceList.add(new Geofence.Builder()
                .setRequestId(title)
                .setCircularRegion(
                        lat,
                        lng,
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    protected void search(List<Address> addresses) {



        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        Toast.makeText(MainActivity.this, address.getLocality(), Toast.LENGTH_LONG).show();
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.setPosition(latLng);
        } else {
            int height = 135;
            int width = 70;
            BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_custom);
            Bitmap b=bitmapDraw.getBitmap();
            Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

            mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(marker_icon)));
        }

    }

    private String getDirectionsUrl1(LatLng origin, Double latitude, Double longitude) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + latitude + "," + longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    // inserts values in the database
    public long insertCurrentParkingPositionToDB(Double latitude, Double longitude, Float zoomLevel) {
        ContentValues dbContent = new ContentValues();
        dbContent.put(ParkingLocationStoreDB.FIELD_LAT, latitude);
        dbContent.put(ParkingLocationStoreDB.FIELD_LNG, longitude);
        dbContent.put(ParkingLocationStoreDB.FIELD_ZOOM, Float.toString(zoomLevel));


        long rowId = locationStoreDB.insert(dbContent);
        return rowId;
    }

    // deletes record from the database
    public int deleteCurrentParkingRecordFromDB() {
        return locationStoreDB.del();
    }

    public static void onParking() {
        parking.callOnClick();
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(MainActivity.this, "Failed to add Geofences", Toast.LENGTH_SHORT).show();

            // Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    status.getStatusCode());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if ( isNetworkConnected()){
                ParserTask parserTask = new ParserTask();
                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            }
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v(TAG, "Internet Connection Not Present");
            return false;
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {


        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < markerPoints.size(); i++) {
            LatLng point = markerPoints.get(i);
            options.add(point);
        }
//        mMap.addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude); //you already have this
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.setPosition(latLng);
        } else {
            int height = 135;
            int width = 70;
            BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_custom);
            Bitmap b=bitmapDraw.getBitmap();
            Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

            mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(marker_icon)));
        }


        markerPoints.add(latLng); //added
//        redrawLine();
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations, this can be null.
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });

        }
    }

    public void setMarker1(Double latitude, Double longitude, float zoomLevel, boolean marked) {
        mMap.clear();
        if (marked) {
            int height = 70;
            int width = 134;
            BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.custom_car_dark_benreaart);
            Bitmap b=bitmapDraw.getBitmap();
            Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
                    longitude)).title("Me").icon(BitmapDescriptorFactory.fromBitmap(marker_icon)));
        } else {
            int height = 70;
            int width = 134;
            BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.custom_car_white_benreaart);
            Bitmap b=bitmapDraw.getBitmap();
            Bitmap marker_icon = Bitmap.createScaledBitmap(b, width, height, false);

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
                    longitude)).title("Me").icon(BitmapDescriptorFactory.fromBitmap(marker_icon)));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));

    }

    // Updates the address in the UI

    protected void displayAddressOutput() {

        try {
            if (mAreaOutput != null)
                mLocationAddress.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Receiver for data sent from FetchAddressIntentService.

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        // Receives data sent from FetchAddressIntentService and updates the UI in MainActivity

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
//                Toast.makeText(MainActivity.this,getString(R.string.address_found), Toast.LENGTH_LONG).show();

            }
        }
    }

    // retreives geographical location
    public String getAddress(Double latitude, Double longitude) {
        Geocoder geocoder;
        List<Address> addresses1 = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses1 = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses1.get(0).getMaxAddressLineIndex() > 0) {
            for (int i = 0; i < addresses1.get(0).getMaxAddressLineIndex(); i++) {
                address1 = addresses1.get(0).getAddressLine(i);
            }
        } else {
            try {
                address1 = addresses1.get(0).getAddressLine(0);
            } catch (Exception ignored) {
            }
        }

        return address1;

    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( mGoogleApiClient == null ) {
            mGoogleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
        if ( mGoogleApiClient != null ){
            Log.d(TAG, "createGoogleApi: OK ");
            mGoogleApiClient.connect();
        }else {
            Log.d(TAG, "createGoogleApi: NULL");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(context, data);

                LatLng latLong;

                latLong = place.getLatLng();

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(15).tilt(70).build();

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


            }
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(context, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }else if (resultCode == 1234) {
            Boolean isClosed = data.getExtras().getBoolean("isClosed",false);
            if (isClosed)
                finish();
        }
    }
}
