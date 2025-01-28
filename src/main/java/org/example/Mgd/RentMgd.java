package org.example.Mgd;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.time.LocalDateTime;

//@JsonSerialize(using = LocalDateTimeSerializer.class)
//@JsonDeserialize(using = LocalDateTimeDeserializer.class)
public class RentMgd extends AbstractEntityMgd {
    @BsonProperty("rentNumber")
    private String rentNumber;
    @BsonProperty("startTime")
    private LocalDateTime startTime;
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
                   @BsonProperty("guestMgd") GuestMgd guest,
                   @BsonProperty("roomMgd") RoomMgd room) {
        super(entityId);
        this.rentNumber = rentNumber;
        this.startTime = startTime;
        this.guest = guest;
        this.room = room;
    }

    public RentMgd() {
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
