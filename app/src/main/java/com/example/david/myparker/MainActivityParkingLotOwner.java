package com.example.david.myparker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
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
import android.widget.ProgressBar;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MainActivityParkingLotOwner extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    GoogleMap mMap;
    String provider;
    FusedLocationProviderClient mFusedLocationClient;
    GeofencingClient client;
    protected ArrayList<Geofence> mGeofenceList;

    NavigationView navView;
    BottomSheetBehavior mBottomSheetBehavior;

    String ExternalFontPath;
    Typeface FontLoaderTypeface;

    private RecordsAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recordsRV;

    String parkingLotId;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_parking_lot_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///--------------------------------------

        View navHeader = navigationView.getHeaderView(0);
        TextView name = navHeader.findViewById(R.id.dname);

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

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        response = getIntent().getExtras().getString("response");
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            parkingLotId = jsonObject.getString("parking_lot_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressBar = findViewById(R.id.progressBar);
        setUpChats();
        downloadParkingRecords();

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(MainActivityParkingLotOwner.this);

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        client = LocationServices.getGeofencingClient(this);

        mGeofenceList = new ArrayList<Geofence>();

        mapFragment.getMapAsync(this);

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "product_sans.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_parking_lot_owner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivityParkingLotOwner.this, SettingsCustomAdmin.class));

        } else if (id == R.id.nav_help) {
            startActivity(new Intent(MainActivityParkingLotOwner.this, Help.class));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivityParkingLotOwner.this, About.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //MAP CODE

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);

        final MapWrapperLayout mapWrapperLayout = findViewById(R.id.map_relative_layout1);
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        // Add a marker in Sydney and move the camera

        if (mMap != null) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            final Location myLocation = locationManager.getLastKnownLocation(provider);
            if (myLocation != null) {
                onLocationChanged(myLocation);
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

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


    private void downloadParkingRecords()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_GET_PARKING_LOTS_RECORDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivityParkingLotOwner.this, "RES : " + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MainActivityParkingLotOwner.Record record = new MainActivityParkingLotOwner.Record(
                                jsonObject.getString("today"),
                                jsonObject.getString("plate_number"),
                                jsonObject.getString("name")
                        );
                        adapter.addRecord(record);

                    }
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityParkingLotOwner.this, "Connection to server failed ...", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("parkingLotId",parkingLotId);
                return data;
            }
        });


    }

    public void setUpChats() {
        recordsRV = findViewById(R.id.recordsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        recordsRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainActivityParkingLotOwner.RecordsAdapter();
        recordsRV.setAdapter(adapter);
    }

    class Record {
        String today;
        String number_plate;
        String userName;

        public Record(String today, String number_plate, String userName) {
            this.today= today;
            this.number_plate = number_plate;
            this.userName = userName;
        }
    }

    class RecordsAdapter extends RecyclerView.Adapter<MainActivityParkingLotOwner.RecordsAdapter.RecordHolder> {

        ArrayList<MainActivityParkingLotOwner.Record> records = new ArrayList<>();

        public void addRecord(MainActivityParkingLotOwner.Record record) {
            records.add(record);
            notifyItemInserted(records.size() - 1);
            recordsRV.scrollToPosition(records.size() - 1);
        }

        @Override
        public MainActivityParkingLotOwner.RecordsAdapter.RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainActivityParkingLotOwner.RecordsAdapter.RecordHolder(LayoutInflater.from(MainActivityParkingLotOwner.this).inflate(R.layout.row_parking_records, parent, false));
        }

        @Override
        public void onBindViewHolder(MainActivityParkingLotOwner.RecordsAdapter.RecordHolder holder, int position) {
            MainActivityParkingLotOwner.Record record = records.get(position);

            holder.today.setText(record.today.split(" ")[1]);
            holder.number_plate.setText(record.number_plate);
            holder.userName.setText(record.userName);

        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        class RecordHolder extends RecyclerView.ViewHolder {
            TextView number_plate;
            TextView userName;
            TextView today;

            public RecordHolder(View itemView) {
                super(itemView);
                today = itemView.findViewById(R.id.today);
                number_plate = itemView.findViewById(R.id.number_plate);
                userName = itemView.findViewById(R.id.userName);
            }
        }
    }

}
