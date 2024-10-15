package org.example;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.example.exception.GuestException;
import org.example.exception.RoomException;
import org.hibernate.annotations.DialectOverride;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)  // Strategia JOINED
@Table(name = "rooms")
public abstract class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private int number;
    @Column
    private int floor;
    @Column
    private double surface;
    @Column
    private boolean balcony;
    @Column
    private double price;
    @Version
    private long version;

    public Room(int number, int floor, double surface, boolean balcony, double price) {
        if (number < 1) {
            throw new RoomException("Room number cannot be lower than 1.");
        } else if (surface <= 0) {
            throw new RoomException("Room surface cannot be 0 or lower.");
        } else if (price < 0) {
            throw new RoomException("Room price cannot be lower than 0.");
        } else {
            this.number = number;
            this.floor = floor;
            this.surface = surface;
            this.balcony = balcony;
            this.price = price;
        }
    }

    public Room() {

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInfo() {
        return "Room number " + String.valueOf(getNumber()) + " on floor " + String.valueOf(getFloor())
                + (isBalcony()?" with balcony.":" without balcony.") + " It has the surface of "
                + String.valueOf(getSurface()) + ". Price per day equals " + String.valueOf(getPrice()) + ".";

    }

    public long getId() {
        return Id;
    }
}

