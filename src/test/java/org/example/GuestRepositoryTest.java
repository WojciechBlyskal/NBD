package org.example;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestRepositoryTest {
    @Test
    public void addGuestTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest = new Guest("Jan", "Kowalski", "123456789");

        guestRepository.addGuest(guest, em);
        List<Guest> list = guestRepository.getGuestByLastName(em, "Kowalski");
        emf.close();
        if (list.size() == 0) {
            fail("It did not add any element to the database.");
        }
        assertTrue(list.get(0).getLastName().equals("Kowalski"));
    }
}
