package org.example.services.Interfaces;

import org.example.model.Guest;

import java.util.List;
import java.util.UUID;

public interface IGuestService {
    Guest createGuest(String name, String lastname, String phonenumber);
    boolean deleteGuest(UUID guestId);
    boolean updateGuest(Guest guest);
    Guest getGuestById(UUID guestId);
    List<Guest> getAllGuests();
}
