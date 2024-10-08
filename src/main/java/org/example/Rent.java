package org.example;

import java.time.LocalDateTime;

public class Rent {
    String rentNumber;
    LocalDateTime startTime;
    LocalDateTime endTime;
    double cost;
    Guest guest;
    Room room;
    //Catering catering

    public Rent(String rentNumber, LocalDateTime startTime, /*Catering catering,*/ Room room) {
        this.rentNumber = rentNumber;
        this.startTime = startTime;
        //this.catering = catering;
        this.room = room;
    }
}
