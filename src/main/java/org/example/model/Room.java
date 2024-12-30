package org.example.model;

import org.example.exception.RoomException;
import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("rooms")
public class Room {
    private int number;
    private int floor;
    private double surface;
    private double price;
    public enum RoomType {
        Studio, MicroSuite
    }

    //@ClusteringColumn
    //@CqlName("type")
    private String type;

    @PartitionKey
    private UUID id;

    @CqlName("rent_ids")
    private Set<UUID> rentIds = new HashSet<>();

    public Room() {
    }

    public Room(int number, int floor, double surface, double price, String type, UUID id, Set<UUID> rentIds) throws RoomException {
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
            this.price = price;
            this.type = type;
            this.id = id;
            this.rentIds = rentIds;
        }
    }

    public Room(int number, int floor, double surface, double price, RoomType type) throws RoomException {
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
            this.price = price;
            this.type = type.name();
            this.id = UUID.randomUUID();
        }
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<UUID> getRentIds() {
        return rentIds;
    }

    public void setRentIds(Set<UUID> rentIds) {
        this.rentIds = rentIds;
    }
}

