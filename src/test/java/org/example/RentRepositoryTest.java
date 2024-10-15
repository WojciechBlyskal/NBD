package org.example;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RentRepositoryTest {
    @Test
    public void addRentTest() { //you cannot test addGuest() without getGuestBySth()
        EntityManagerFactory emf6 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em6 = emf6.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest5 = new Guest("Jan", "Kowalski", "123456789");
        Guest guest6 = new Guest("Piotr", "Kowalski", "987654321");
        RoomRepository roomRepository = new RoomRepository();
        Studio room5 = new Studio(1, 0, 25.0, false, 180.0);
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent = new Rent("A34", startTime, room5, guest5);
        Rent rent0 = new Rent("A34", startTime, room5, guest6);

        guestRepository.addGuest(guest5, em6);
        guestRepository.addGuest(guest6, em6);
        roomRepository.addRoom(room5, em6);
        rentRepository.addRent(rent, em6);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            rentRepository.addRent(rent0, em6);
        });
        assertEquals("This room is currently rented and cannot be rented to anybody else.", exception.getMessage());
        em6.getTransaction().begin();
        List<Rent> list6 = rentRepository.getRentByRentNumber(em6, "A34");
        em6.getTransaction().commit();
        if (list6.size() == 0) {
            fail("It did not add any element to the database.");
        } else if (list6.size() != 1) {
            fail("It did not prohibit multiple rents on the same room ");
        }
        assertTrue(list6.contains(rent));

        LocalDateTime endTime = LocalDateTime.of(2024, 10, 18, 14, 30, 45);
        em6.getTransaction().begin();
        rentRepository.getRentByRentNumber(em6, "A34").get(0).setEndTime(endTime);
        em6.getTransaction().commit();
        rentRepository.addRent(rent0, em6);
        em6.getTransaction().begin();
        list6 = rentRepository.getRentByRentNumber(em6, "A34");
        em6.getTransaction().commit();
        emf6.close();
        assertTrue(list6.contains(rent0));
    }

    @Test
    public void getAllRentsTest() {
        EntityManagerFactory emf7 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em7 = emf7.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest7 = new Guest("Jan", "Kowalski", "123456789");
        Guest guest8 = new Guest("Piotr", "Kowalski", "987654321");
        RoomRepository roomRepository = new RoomRepository();
        Studio room6 = new Studio(1, 0, 25.0, false, 180.0);
        MicroSuite room7 = new MicroSuite(1, 2, 45.0, true, 250.0);
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent1 = new Rent("A34", startTime, room6, guest7);
        Rent rent2 = new Rent("A34", startTime, room7, guest8);

        guestRepository.addGuest(guest7, em7);
        guestRepository.addGuest(guest8, em7);
        roomRepository.addRoom(room6, em7);
        roomRepository.addRoom(room7, em7);
        rentRepository.addRent(rent1, em7);
        rentRepository.addRent(rent2, em7);

        List<Rent> list7 = rentRepository.getAllRents(em7);
        if (list7.size() != 2) {
            fail("It did not add all elements to the database.");
        }
        assertTrue(list7.contains(rent1));
        assertTrue(list7.contains(rent2));
    }

    @Test
    public void getAllRentsByGuestTest() {
        EntityManagerFactory emf8 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em8 = emf8.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest9 = new Guest("Jan", "Kowalski", "123456789");
        RoomRepository roomRepository = new RoomRepository();
        Studio room8 = new Studio(1, 0, 25.0, false, 180.0);
        MicroSuite room9 = new MicroSuite(1, 2, 45.0, true, 250.0);
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent3 = new Rent("A34", startTime, room8, guest9);
        Rent rent4 = new Rent("A35", startTime, room9, guest9);

        guestRepository.addGuest(guest9, em8);
        roomRepository.addRoom(room8, em8);
        roomRepository.addRoom(room9, em8);
        rentRepository.addRent(rent3, em8);
        rentRepository.addRent(rent4, em8);
        em8.getTransaction().begin();
        List<Rent> list8 = rentRepository.getAllRentsByGuest(em8, 1);
        em8.getTransaction().commit();
        if (list8.size() != 2) {
            fail("It did not add all elements to the database.");
        }
        assertTrue(list8.contains(rent3));
        assertTrue(list8.contains(rent4));
    }
}
