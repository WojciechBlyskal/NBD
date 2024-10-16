package org.example;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void updateGuestNameTest() {
        EntityManagerFactory emf50 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em50 = emf50.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest50 = new Guest("Jan", "Kowalski", "123456789");

        guestRepository.addGuest(guest50, em50);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            guestRepository.updateGuestName(em50, 1, " ");
        });
        assertEquals(exception.getMessage(), "Failed to update guest's name: New name cannot be blank.");

        guestRepository.updateGuestName(em50, 1, "Marcin");
        String name = guestRepository.getGuestName(em50, 1);
        assertEquals("Marcin", name);
    }

    @Test
    public void updateGuestLastNameTest() {
        EntityManagerFactory emf51 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em51 = emf51.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest51 = new Guest("Jan", "Kowalski", "123456789");

        guestRepository.addGuest(guest51, em51);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            guestRepository.updateGuestLastName(em51, 1, " ");
        });
        assertEquals(exception.getMessage(), "Failed to update guest's last name: New last name cannot be blank.");

        guestRepository.updateGuestLastName(em51, 1, "Nowak");
        String lastName = guestRepository.getGuestLastName(em51, 1);
        assertEquals("Nowak", lastName);
    }

    @Test
    public void updateGuestPhoneNumberTest() {
        EntityManagerFactory emf52 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em52 = emf52.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest52 = new Guest("Jan", "Kowalski", "123456789");

        guestRepository.addGuest(guest52, em52);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            guestRepository.updateGuestPhoneNumber(em52, 1, " ");
        });
        assertEquals(exception.getMessage(), "Failed to update guest's phone number: New phone number cannot be blank.");

        guestRepository.updateGuestPhoneNumber(em52, 1, "987654321");
        String phoneNumber = guestRepository.getGuestPhoneNumber(em52, 1);
        assertEquals("987654321", phoneNumber);
    }
}
