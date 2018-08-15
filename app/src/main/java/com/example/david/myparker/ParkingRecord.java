package com.example.david.myparker;

/**
 * Created by David on 6/14/2018.
 */

public class ParkingRecord
{
    String parking_lot_id;
    String parking_record_id;
    String name;
    String plate_number;
    String today;
    String gps_codes;

    public ParkingRecord(String parking_lot_id, String parking_record_id, String name, String plate_number, String today, String gps_codes) {
        this.parking_lot_id = parking_lot_id;
        this.parking_record_id = parking_record_id;
        this.name = name;
        this.plate_number = plate_number;
        this.today = today;
        this.gps_codes = gps_codes;
    }
}
