package org.example;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RentRepositoryTest {
    @Test
    public void addRentGetTenByNumberTest() {
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
        List<Rent> list6 = rentRepository.getRentByRentNumber(em6, "A34");
        if (list6.size() == 0) {
            fail("It did not add any element to the database.");
        } else if (list6.size() != 1) {
            fail("It did not prohibit multiple rents on the same room ");
        }
        assertTrue(list6.contains(rent));

        LocalDateTime endTime = LocalDateTime.of(2024, 10, 18, 14, 30, 45);
        rentRepository.getRentByRentNumber(em6, "A34").get(0).setEndTime(endTime);
        rentRepository.addRent(rent0, em6);
        list6 = rentRepository.getRentByRentNumber(em6, "A34");
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
        List<Rent> list8 = rentRepository.getAllRentsByGuest(em8, 1);
        if (list8.size() != 2) {
            fail("It did not add all elements to the database.");
        }
        assertTrue(list8.contains(rent3));
        assertTrue(list8.contains(rent4));
    }

    @Test
    public void getRentNumberTest() {
        EntityManagerFactory emf70 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em70 = emf70.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest70 = new Guest("Jan", "Kowalski", "123456789");
        RoomRepository roomRepository = new RoomRepository();
        Studio room70 = new Studio(1, 0, 25.0, false, 180.0);
        LocalDateTime startTime70 = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent70 = new Rent("A34", startTime70, room70, guest70);

        guestRepository.addGuest(guest70, em70);
        roomRepository.addRoom(room70, em70);
        rentRepository.addRent(rent70, em70);

        String rentNumber = rentRepository.getRentNumber(em70, 1);
        assertEquals("A34", rentNumber);
    }

    @Test
    public void getRentStartTimeTest() {
        EntityManagerFactory emf71 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em71 = emf71.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest71 = new Guest("Jan", "Kowalski", "123456789");
        RoomRepository roomRepository = new RoomRepository();
        Studio room71 = new Studio(1, 0, 25.0, false, 180.0);
        LocalDateTime startTime71 = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent71 = new Rent("A34", startTime71, room71, guest71);

        guestRepository.addGuest(guest71, em71);
        roomRepository.addRoom(room71, em71);
        rentRepository.addRent(rent71, em71);

        LocalDateTime start = rentRepository.getRentStartTime(em71, 1);
        assertEquals(startTime71, start);
    }

    @Test
    public void getRentEndTimeTest() {
        EntityManagerFactory emf72 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em72 = emf72.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest72 = new Guest("Jan", "Kowalski", "123456789");
        RoomRepository roomRepository = new RoomRepository();
        Studio room72 = new Studio(1, 0, 25.0, false, 180.0);
        LocalDateTime startTime71 = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent72 = new Rent("A34", startTime71, room72, guest72);

        guestRepository.addGuest(guest72, em72);
        roomRepository.addRoom(room72, em72);
        rentRepository.addRent(rent72, em72);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            rentRepository.updateRentEndTime(em72, 1, null);
        });
        assertEquals("Failed to set rent's end time: New end time cannot be null.", exception.getMessage());

        LocalDateTime endTimexx = LocalDateTime.of(2024, 10, 10, 14, 30, 45);
        Exception exception1 = assertThrows(RuntimeException.class, () -> {
            rentRepository.updateRentEndTime(em72, 1, endTimexx);
        });
        assertEquals("Failed to set rent's end time: New end time cannot be before start time.", exception1.getMessage());

        LocalDateTime endTime72 = LocalDateTime.of(2024, 10, 18, 14, 30, 45);
        rentRepository.updateRentEndTime(em72, 1, endTime72);
        assertEquals(endTime72, rentRepository.getRentEndTime(em72, 1));

        LocalDateTime endTimeWrong = LocalDateTime.of(2024, 10, 20, 14, 30, 45);
        Exception exception2 = assertThrows(RuntimeException.class, () -> {
            rentRepository.updateRentEndTime(em72, 1, endTimeWrong);
        });
        assertEquals("Failed to set rent's end time: End time once set cannot be changed.", exception2.getMessage());
    }

    @Test
    public void getRentRoomTest() {
        EntityManagerFactory emf73 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em73 = emf73.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest73 = new Guest("Jan", "Kowalski", "123456789");
        RoomRepository roomRepository = new RoomRepository();
        Studio room73 = new Studio(1, 0, 25.0, false, 180.0);
        LocalDateTime startTime73 = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent73 = new Rent("A34", startTime73, room73, guest73);

        guestRepository.addGuest(guest73, em73);
        roomRepository.addRoom(room73, em73);
        rentRepository.addRent(rent73, em73);

        Room roomx = rentRepository.getRentRoom(em73, 1);
        assertEquals(room73, roomx);
    }

    @Test
    public void getRentRGuestTest() {
        EntityManagerFactory emf74 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em74 = emf74.createEntityManager();
        GuestRepository guestRepository = new GuestRepository();
        Guest guest74 = new Guest("Jan", "Kowalski", "123456789");
        RoomRepository roomRepository = new RoomRepository();
        Studio room74 = new Studio(1, 0, 25.0, false, 180.0);
        LocalDateTime startTime74 = LocalDateTime.of(2024, 10, 15, 14, 30, 45);
        RentRepository rentRepository = new RentRepository();
        Rent rent74 = new Rent("A34", startTime74, room74, guest74);

        guestRepository.addGuest(guest74, em74);
        roomRepository.addRoom(room74, em74);
        rentRepository.addRent(rent74, em74);

        Guest guestx = rentRepository.getRentGuest(em74, 1);
        assertEquals(guest74, guestx);
    }
}
