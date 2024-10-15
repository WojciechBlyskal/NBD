package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import jakarta.persistence.*;

import org.example.exception.GuestException;
import org.example.exception.RentException;

@Entity
@Table(name = "rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private String rentNumber;
    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;
    @Column
    private double cost;
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;


    public Rent(String rentNumber, LocalDateTime startTime, Room room, Guest guest) {
        if (rentNumber.isBlank()) {
            throw new RentException("Rent number cannot be empty.");
        } else if (startTime == null) {
            throw new RentException(" Improper start time.");
        } else if (room.equals(null)) {
            throw new RentException("Invalid room.");
        } else if (guest.equals(null)) {
            throw new RentException("Invalid guest.");
        } else {
            this.rentNumber = rentNumber;
            this.startTime = startTime;
            this.room = room;
            this.guest = guest;
        }
    }

    public Rent() {

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

    public void setEndTime(LocalDateTime endTime) {
        if (endTime == null) {
            this.endTime = endTime;
        }
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
        return "Your rent id number is " + getRentNumber() + ".The rent started on " + String.valueOf(getStartTime())
                + "Here's some info about your room: " + getRoom().getInfo() + "\nInfo about the guests: " + getGuest().getInfo();
    }

    public long getId() {
        return Id;
    }
}