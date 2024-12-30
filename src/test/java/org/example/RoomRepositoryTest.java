package org.example;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import org.example.cassandra.CassandraRepository;
import org.example.model.Room;
import org.example.repositories.EntityRepository;
import org.example.repositories.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomRepositoryTest {
    private EntityRepository<Room> roomRepository;
    protected CassandraRepository cassandraRepository;
    protected CqlSession session;

    @BeforeEach
    public void setUp() {
        cassandraRepository = new CassandraRepository();
        session = cassandraRepository.getSession();
        roomRepository = new RoomRepository(session, CqlIdentifier.fromCql("site"));
        session.execute("TRUNCATE site.rooms;");
        session.execute("DROP TABLE IF EXISTS site.rooms;");
    }

    @AfterEach
    public void tearDown() {
        if (cassandraRepository != null) {
            cassandraRepository.close();
        }
    }

    @Test
    void createGetByIdTest() {
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        roomRepository.create(room);

        Room retrievedRoom = roomRepository.getById(room.getId());
        assertEquals(room.getId(), retrievedRoom.getId());
    }

    @Test
    void getAllTest() {
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        roomRepository.create(room);
        Room room2 = new Room(5, 1, 26.0, 100.0, Room.RoomType.MicroSuite);
        roomRepository.create(room2);

        List<Room> retrievedRooms = roomRepository.getAll();
        assertEquals(2, retrievedRooms.size());
        assertTrue(retrievedRooms.stream().anyMatch(r -> r.getId().equals(room.getId())));
        assertTrue(retrievedRooms.stream().anyMatch(r -> r.getId().equals(room2.getId())));
    }

    @Test
    void updateTest() {
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        roomRepository.create(room);
        Room retrievedRoom = roomRepository.getById(room.getId());
        assertEquals(room.getId(), retrievedRoom.getId()); //make sure there was such a room
        assertEquals(2, retrievedRoom.getNumber());
        assertEquals(3, retrievedRoom.getFloor());
        assertEquals(12.0, retrievedRoom.getSurface());
        assertEquals(25.0, retrievedRoom.getPrice());
        assertEquals("Studio", retrievedRoom.getType());

        room.setNumber(12); //actual update test
        room.setFloor(5);
        room.setSurface(45.0);
        room.setPrice(50.0);
        room.setType("MicroSuite");
        roomRepository.update(room);
        Room updatedRoom = roomRepository.getById(room.getId());
        assertEquals(12, updatedRoom.getNumber());
        assertEquals(5, updatedRoom.getFloor());
        assertEquals(45.0, updatedRoom.getSurface());
        assertEquals(50.0, updatedRoom.getPrice());
        assertEquals("MicroSuite", updatedRoom.getType());
    }

    @Test
    void deleteTest() {
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        roomRepository.create(room);
        Room retrievedRoom = roomRepository.getById(room.getId());
        assertEquals(room.getId(), retrievedRoom.getId()); //make sure there was such a room

        roomRepository.delete(retrievedRoom);
        Room deletedRoom = roomRepository.getById(room.getId());
        assertNull(deletedRoom);
    }
}
