package com.example.david.myparker;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminParkingLotsActivity extends AppCompatActivity {

    String ExternalFontPath;
    Typeface FontLoaderTypeface;

    private RecordsAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recordsRV;

    String parkingLotId;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parking_lots);

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
        TextView textview = new TextView(AdminParkingLotsActivity.this);

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

    private void downloadParkingRecords()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.POST, MainData.URL_GET_PARKING_LOTS_RECORDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AdminParkingLotsActivity.this, "RES : " + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Record record = new Record(
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
                Toast.makeText(AdminParkingLotsActivity.this, "Connection to server failed ...", Toast.LENGTH_SHORT).show();

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
        adapter = new RecordsAdapter();
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

    class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordHolder> {

        ArrayList<Record> records = new ArrayList<>();

        public void addRecord(Record record) {
            records.add(record);
            notifyItemInserted(records.size() - 1);
            recordsRV.scrollToPosition(records.size() - 1);
        }

        @Override
        public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecordHolder(LayoutInflater.from(AdminParkingLotsActivity.this).inflate(R.layout.row_parking_records, parent, false));
        }

        @Override
        public void onBindViewHolder(RecordHolder holder, int position) {
            Record record = records.get(position);

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
