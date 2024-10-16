package org.example;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomRepositoryTest {
    @Test
    public void addRoomGetRoomByNumberTest() {
        EntityManagerFactory emf3 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em3 = emf3.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room = new Studio(1, 0, 25.0, false, 180.0);
        MicroSuite room0 = new MicroSuite(1, 2, 45.0, true, 250.0);

        roomRepository.addRoom(room, em3);
        roomRepository.addRoom(room0, em3);
        List<Room> list3 = roomRepository.getRoomByNumber(em3, 1);
        emf3.close();
        if (list3.size() == 0) {
            fail("It did not add any element to the database.");
        }
        assertTrue(list3.contains(room));
        assertTrue(list3.contains(room0));
    }

    @Test
    public void getRoomByIdTest() {
        EntityManagerFactory emf4 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em4 = emf4.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room1 = new Studio(1, 0, 25.0, false, 180.0);

        roomRepository.addRoom(room1, em4);
        List<Room> list4 = roomRepository.getRoomById(em4, 1);
        emf4.close();
        if (list4.size() == 0) {
            fail("It did not add any element to the database.");
        }
        assertTrue(list4.get(0).equals(room1));
    }

    @Test
    public void getAllRoomsTest() {
        EntityManagerFactory emf5 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em5 = emf5.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room2 = new Studio(1, 0, 25.0, false, 180.0);
        MicroSuite room3 = new MicroSuite(1, 2, 45.0, true, 250.0);
        MicroSuite room4 = new MicroSuite(2, 1, 55.0, true, 270.0);

        roomRepository.addRoom(room2, em5);
        roomRepository.addRoom(room3, em5);
        roomRepository.addRoom(room4, em5);
        List<Room> list5 = roomRepository.getAllRooms(em5);
        emf5.close();
        if (list5.size() != 3) {
            fail("It did not add all elements to the database.");
        }
        assertTrue(list5.contains(room2));
        assertTrue(list5.contains(room3));
        assertTrue(list5.contains(room4));
    }

    @Test
    public void updateRoomNumberTest() {
        EntityManagerFactory emf60 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em60 = emf60.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room60 = new Studio(1, 0, 25.0, false, 180.0);

        roomRepository.addRoom(room60, em60);
        roomRepository.updateRoomNumber(em60, 1, 5);
        List<Room> list60 = roomRepository.getRoomByNumber(em60, 5);
        if (list60.size() == 0) {
            fail("It did not change value in the database.");
        }
        assertEquals(5, list60.get(0).getNumber());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            roomRepository.updateRoomNumber(em60, 1, -1);
        });
        assertEquals("Failed to update room number: New room number cannot be negative.", exception.getMessage());
    }

    @Test
    public void updateRoomPriceTest() {
        EntityManagerFactory emf61 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em61 = emf61.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room61 = new Studio(1, 0, 25.0, false, 180.0);

        roomRepository.addRoom(room61, em61);
        roomRepository.updateRoomPrice(em61, 1, 200.0);
        double price = roomRepository.getRoomPrice(em61, 1);
        assertEquals(200.0, price);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            roomRepository.updateRoomPrice(em61, 1, -10.0);
        });
        assertEquals("Failed to update room's price: New price cannot be negative.", exception.getMessage());
    }

    @Test
    public void getRoomFloorTest() {
        EntityManagerFactory emf62 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em62 = emf62.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room62 = new Studio(1, 5, 25.0, false, 180.0);

        roomRepository.addRoom(room62, em62);
        int floor = roomRepository.getRoomFloor(em62, 1);
        assertEquals(5, floor);
    }

    @Test
    public void getRoomSurfaceTest() {
        EntityManagerFactory emf63 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em63 = emf63.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room63 = new Studio(1, 5, 25.0, false, 180.0);

        roomRepository.addRoom(room63, em63);
        double surface = roomRepository.getRoomSurface(em63, 1);
        assertEquals(25.0, surface);
    }

    @Test
    public void isRoomBalconyTest() {
        EntityManagerFactory emf64 = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em64 = emf64.createEntityManager();
        RoomRepository roomRepository = new RoomRepository();
        Studio room64 = new Studio(1, 5, 25.0, true, 180.0);

        roomRepository.addRoom(room64, em64);
        boolean balcony = roomRepository.isRoomBalcony(em64, 1);
        assertEquals(true, balcony);
    }
}
