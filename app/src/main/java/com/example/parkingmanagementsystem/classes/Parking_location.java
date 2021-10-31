package com.example.parkingmanagementsystem.classes;

public class Parking_location {
    private String id;
    private String name;

    public Parking_location(String id, String name) {
        this.id = id;
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Parking_location{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
