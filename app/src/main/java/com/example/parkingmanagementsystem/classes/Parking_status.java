package com.example.parkingmanagementsystem.classes;

public class Parking_status {
    private String slot_id;
    private String status;

    public Parking_status(String slot_id, String status) {
        this.slot_id = slot_id;
        this.status = status;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Parking_status{" +
                "slot_id='" + slot_id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
