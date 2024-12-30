package org.example.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.exception.RentException;

@Entity(defaultKeyspace = "site")
@CqlName("rents")
public class Rent {
    @PartitionKey
    private UUID id;

    @CqlName("start_time")
    private LocalDate startTime;
    @CqlName("end_time")
    private LocalDate endTime;
    @CqlName("guest")
    private UUID guestIds;
    @CqlName("room")
    private UUID roomIds;

    public Rent() {
    }

    public Rent(LocalDate startTime, LocalDate endTime, UUID guestIds, UUID roomIds, UUID id) throws RentException {
        if (startTime == null) {
            throw new RentException(" Improper start time.");
        } else if (endTime == null) {
            throw new RentException(" Improper end time.");
        } else {
            this.startTime = startTime;
            this.endTime = endTime;
            this.id = id;
            this.guestIds = guestIds;
            this.roomIds = roomIds;
        }
    }

    public Rent(LocalDate startTime, LocalDate endTime, Guest guest, Room room) throws RentException {
        if (startTime == null) {
            throw new RentException(" Improper start time.");
        } else if (endTime == null) {
            throw new RentException(" Improper end time.");
        } else {
            this.startTime = startTime;
            this.endTime = endTime;
            this.id = UUID.randomUUID();
            this.guestIds = guest.getId();
            this.roomIds = room.getId();
        }
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGuestIds() {
        return guestIds;
    }

    public UUID getRoomIds() {
        return roomIds;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public void setGuestIds(UUID guestIds) {
        this.guestIds = guestIds;
    }

    public void setRoomIds(UUID roomIds) {
        this.roomIds = roomIds;
    }
}
