package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.time.LocalDateTime;

public class RentMgd extends AbstractEntityMgd {
    @BsonProperty("rentNumber")
    private String rentNumber;
    @BsonProperty("startTime")
    private LocalDateTime startTime;
    @BsonProperty("endTime")
    private LocalDateTime endTime;
    @BsonProperty("cost")
    private double cost;
    @BsonProperty("guestMgd")
    private GuestMgd guest;
    @BsonProperty("roomMgd")
    private RoomMgd room;

    @BsonCreator
    public RentMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                   @BsonProperty("rentNumber") String rentNumber,
                   @BsonProperty("startTime") LocalDateTime startTime,
                   @BsonProperty("endTime") LocalDateTime endTime,
                   @BsonProperty("cost") double cost,
                   @BsonProperty("guestMgd") GuestMgd guest,
                   @BsonProperty("roomMgd") RoomMgd room) {
        super(entityId);
        this.rentNumber = rentNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.guest = guest;
        this.room = room;
    }

    public String getRentNumber() {
        return rentNumber;
    }

    public void setRentNumber(String rentNumber) {
        this.rentNumber = rentNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public GuestMgd getGuest() {
        return guest;
    }

    public void setGuest(GuestMgd guest) {
        this.guest = guest;
    }

    public RoomMgd getRoom() {
        return room;
    }

    public void setRoom(RoomMgd room) {
        this.room = room;
    }
}
