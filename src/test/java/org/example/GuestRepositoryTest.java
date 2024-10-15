package org.example;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class GuestRepositoryTest {
    @Test
    public void addGuestGetGuestByIdTest() { //you cannot test addGuest() without getGeustBySth()
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        Guest guest0 = new Guest("Piotr", "Kowalski", "987654321");

        guestRepository.addGuest(guest, em);
        guestRepository.addGuest(guest0, em);
        List<Guest> list = guestRepository.getGuestByLastName(em, "Kowalski");
        emf.close();
        if (list.size() == 0) {
            fail("It did not add any element to the database.");
        }
        assertTrue(list.contains(guest));
        assertTrue(list.contains(guest0));
    }

    @Test
    public void getGuestByIdTest() {
        EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em1 = emf1.createEntityManager();
        GuestRepository guestRepository1 = new GuestRepository();
        Guest guest1 = new Guest("Jan", "Kowalski", "123456789");
        guestRepository1.addGuest(guest1, em1);
        //as long as guest1 becomes the first (ever!!!) element of the database guest1 will get id == 1
        List<Guest> list1 = guestRepository1.getGuestById(em1, 1);
        emf1.close();
        if (list1.size() == 0) {
            fail("It did not add any element to the database.");
        }
        assertTrue(list1.get(0).getId() == 1);
    }

    @Test
    public void getAllGuests() {
        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em2 = emf2.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest2 = new Guest("Marcin", "Kowalski", "123456789");
        Guest guest3 = new Guest("Piotr", "Kowalski", "987654321");
        Guest guest4 = new Guest("Jan", "Pawel", "2");

        guestRepository.addGuest(guest2, em2);
        guestRepository.addGuest(guest3, em2);
        guestRepository.addGuest(guest4, em2);
        List<Guest> list2 = guestRepository.getAllGuests(em2);
        emf2.close();
        if (list2.size() != 3) {
            fail("It did not add all elements in the database.");
        }
        assertTrue(list2.contains(guest2));
        assertTrue(list2.contains(guest3));
        assertTrue(list2.contains(guest4));
    }
}
