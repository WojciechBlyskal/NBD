package org.example;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestRepositoryTest {
    @Test
    public void addGuestTest() {

        GuestRepository guestRepository = new GuestRepository();
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        guestRepository.addGuest(guest);
    }
}
