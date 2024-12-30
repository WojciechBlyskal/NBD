package org.example.services;

import org.example.model.Guest;
import org.example.repositories.GuestRepository;
//import org.example.services.Interfaces.IGuestService;

import java.util.List;
import java.util.UUID;

public class GuestService /* implements IGuestService */{
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    //@Override
    public Guest createGuest(String name, String lastname, String phonenumber) {
        try {
            Guest guest = new Guest(name, lastname, phonenumber);
            guestRepository.create(guest);
            return guest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //@Override
    public boolean deleteGuest(UUID guestId) {
        try {
            Guest guest = guestRepository.getById(guestId);
            if (guest == null) {
                return false;
            }
            guestRepository.delete(guest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //@Override
    public boolean updateGuest(Guest guest) {
        try {
            Guest existingGuest = guestRepository.getById(guest.getId());
            if (existingGuest == null) {
                return false;
            }

            guestRepository.update(guest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //@Override
    public Guest getGuestById(UUID guestId) {
        return guestRepository.getById(guestId);
    }

    //@Override
    public List<Guest> getAllGuests() {
        return guestRepository.getAll();
    }
}
