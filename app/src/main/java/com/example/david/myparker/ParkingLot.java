package com.example.david.myparker;

/*** Created by David on 6/14/2018.*/

public class ParkingLot {
    String parking_lot_id;
    String name;
    String address;
    String description;
    String gpscodes;
    String number_of_slots;
    String email;
    String phone_number;
    String password;
    String photo_external;
    String photo_internal;
    String number_of_used_slots;


    public ParkingLot(String parking_lot_id, String name, String address, String description, String gpscodes, String number_of_slots, String email, String phone_number, String password, String photo_external, String photo_internal, String number_of_used_slots) {
        this.parking_lot_id = parking_lot_id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.gpscodes = gpscodes;
        this.number_of_slots = number_of_slots;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.photo_external = photo_external;
        this.photo_internal = photo_internal;
        this.number_of_used_slots = number_of_used_slots;
    }
}
