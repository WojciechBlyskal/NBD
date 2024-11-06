package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.time.LocalDateTime;

public class RentMgd extends AbstractEntityMgd {
    @BsonProperty("rentNumber")
    private String rentNumber;
    @BsonProperty("startTime")
    private final LocalDateTime startTime;
    @BsonProperty("endTime")
    private LocalDateTime endTime = null;
    @BsonProperty("guestMgd")
    private GuestMgd guest;
    @BsonProperty("roomMgd")
    private RoomMgd room;

    @BsonCreator
    public RentMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                   @BsonProperty("rentNumber") String rentNumber,
                   @BsonProperty("startTime") LocalDateTime startTime,
                   //@BsonProperty("endTime") LocalDateTime endTime,
                   @BsonProperty("guestMgd") GuestMgd guest,
                   @BsonProperty("roomMgd") RoomMgd room) {
        super(entityId);
        this.rentNumber = rentNumber;
        this.startTime = startTime;
        //this.endTime = endTime;
        //this.guest = guest;
        //this.room = room;
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

    /*public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }*/

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime != null && this.endTime == null && endTime.isAfter(startTime)) {
            this.endTime = endTime;
        }
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
