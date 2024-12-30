package org.example.services.Interfaces;

import org.example.model.Guest;
import org.example.model.Rent;
import org.example.model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IRentService {
    Rent addRent(LocalDate startDate, Guest guest, Room room);
    boolean deleteRent(UUID rentId, UUID userId);
    boolean updateRent(Rent rent, UUID userId);
    Rent getRentById(UUID rentId);
    List<Rent> getAllRents();
}