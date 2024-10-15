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
        em3.getTransaction().begin();
        List<Room> list3 = roomRepository.getRoomByNumber(em3, 1);
        em3.getTransaction().commit();
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
        em4.getTransaction().begin();
        List<Room> list4 = roomRepository.getRoomById(em4, 1);
        em4.getTransaction().commit();
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
}
