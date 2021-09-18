package com.example.springandsummergolfclub;

public class GolfClub {

    String names, description, details;
    int imageId;

    public GolfClub(String name, int imageId, String description, String details) {
        this.names = name;
        this.description = description;
        this.imageId = imageId;
        this.details = details;
    }
}
