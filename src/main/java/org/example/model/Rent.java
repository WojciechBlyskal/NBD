package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;

import org.example.exception.RentException;

public class Rent {
    private String rentNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double cost;
    private Guest guest;
    private Room room;
    private Catering catering;

    public Rent(String rentNumber, LocalDateTime startTime, Catering catering, Room room, Guest guest) {
        if (rentNumber.isBlank()) {
            throw new RentException("Rent number cannot be empty.");
        } else if (startTime == null) {
            throw new RentException(" Improper start time.");
        } else if (catering.equals(null)) {
            throw new RentException("Invalid catering.");
        } else if (room.equals(null)) {
            throw new RentException("Invalid room.");
        } else if (guest.equals(null)) {
            throw new RentException("Invalid guest.");
        } else {
            this.rentNumber = rentNumber;
            this.startTime = startTime;
            this.catering = catering;
            this.room = room;
            this.guest = guest;
        }
    }

    public String getRentNumber() {
        return rentNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getCost() {
        return cost;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public Catering getCatering() {
        return catering;
    }

    public void changeCatering(Catering new_catering) {
        if (new_catering.equals(null)) {
            throw new RentException("Catering cannot be null");
        }
        else {
            catering = new_catering;
        }
    }

    public int getRentDays() {
        boolean isCanceled = false;
        boolean isFinished = false;

        if (getEndTime() != null) {
            isFinished = true;
        }
        if (isFinished) {
            Duration period = Duration.between(getStartTime(), getEndTime());
            if (period.toHours() == 0 && period.toMinutes() < 1) {
                isCanceled = true;
            }
            if (isCanceled) {
                return 0;
            }
            return (int) (period.toHours() / 24) + 1;
        }
        return 0;
    }

    public String getInfo() {
        return "Your rent id number is " + getRentNumber()+".The rent started on " + String.valueOf(getStartTime())
                + "Here's some info about your room: " + getRoom().getInfo() + "\nInfo about the catering: "
            + catering.getCateringInfo() + "\nInfo about the guests: " + getGuest().getInfo();
    }
}
