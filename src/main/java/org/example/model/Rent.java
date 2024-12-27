package org.example.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.exception.RentException;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("rents")
public class Rent {
    @PartitionKey
    private UUID id;
    private LocalDate startTime;
    private LocalDate endTime;
    @CqlName("rent_by_guest")
    private Set<UUID> guestIds = new HashSet<>();
    @CqlName("rent_by_room")
    private Set<UUID> roomIds = new HashSet<>();

    public Rent(LocalDate startTime, Set<UUID> guestIds, Set<UUID> roomIds, UUID id) throws RentException {
        if (startTime == null) {
            throw new RentException(" Improper start time.");
        } else {
            this.startTime = startTime;
            this.id = id;
        }
    }

    public Rent(LocalDate startTime, Room room, Guest guest) throws RentException {
        if (startTime == null) {
            throw new RentException(" Improper start time.");
        }else {
            this.startTime = startTime;
            this.id = UUID.randomUUID();
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
}
