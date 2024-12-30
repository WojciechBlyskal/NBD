package org.example;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import org.example.cassandra.CassandraRepository;
import org.example.model.Guest;
import org.example.model.Rent;
import org.example.model.Room;
import org.example.repositories.EntityRepository;
import org.example.repositories.RentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RentRepositoryTest {
    private EntityRepository<Rent> rentRepository;
    protected CassandraRepository cassandraRepository;
    protected CqlSession session;

    @BeforeEach
    public void setUp() {
        cassandraRepository = new CassandraRepository();
        session = cassandraRepository.getSession();
        rentRepository = new RentRepository(session, CqlIdentifier.fromCql("site"));
        session.execute("TRUNCATE site.rents;"); //getAllTest nie przechodzi bez tego
    }

    @AfterEach
    public void tearDown() {
        if (cassandraRepository != null) {
            cassandraRepository.close();
        }
    }

    @Test
    void createGetByIdTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);
        Rent rent = new Rent(startDate, endDate, guest, room);
        rentRepository.create(rent);

        Rent retrievedRent = rentRepository.getById(rent.getId());
        assertEquals(rent.getId(), retrievedRent.getId());
    }

    @Test
    void getAllTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);
        Rent rent = new Rent(startDate, endDate, guest, room);

        Guest guest2 = new Guest("Wojciech", "Laksylb", "123123123");
        Room room2 = new Room(5, 1, 26.0, 100.0, Room.RoomType.MicroSuite);
        LocalDate startDate2 = LocalDate.of(2024, 10, 1);
        LocalDate endDate2 = LocalDate.of(2024, 10, 7);
        Rent rent2 = new Rent(startDate2, endDate2, guest2, room2);

        rentRepository.create(rent);
        rentRepository.create(rent2);

        List<Rent> rents = rentRepository.getAll();

        assertEquals(2, rents.size());
        assertTrue(rents.stream().anyMatch(r -> r.getId().equals(rent.getId())));
        assertTrue(rents.stream().anyMatch(r -> r.getId().equals(rent2.getId())));
    }

    @Test
    void updateTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);
        Rent rent = new Rent(startDate, endDate, guest, room);
        rentRepository.create(rent);
        Rent retrievedRent = rentRepository.getById(rent.getId());
        assertEquals(rent.getId(), retrievedRent.getId()); //make sure there was such a guest
        assertEquals(guest.getId(), retrievedRent.getGuestIds());
        assertEquals(room.getId(), retrievedRent.getRoomIds());
        assertEquals(startDate, retrievedRent.getStartTime());

        UUID guestId1 = UUID.randomUUID();
        UUID roomId1 = UUID.randomUUID();
        LocalDate startDate2 = LocalDate.of(2024, 10, 1);
        LocalDate endDate2 = LocalDate.of(2024, 10, 7);
        rent.setStartTime(startDate2); //actual update test
        rent.setEndTime(endDate2);
        rent.setGuestIds(guestId1);
        rent.setRoomIds(roomId1);
        rentRepository.update(rent);
        Rent updatedRent = rentRepository.getById(rent.getId());
        assertEquals(startDate2, updatedRent.getStartTime());
        assertEquals(guestId1, updatedRent.getGuestIds());
        assertEquals(roomId1, updatedRent.getRoomIds());
    }

    @Test
    void deleteTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        Room room = new Room(2, 3, 12.0, 25.0, Room.RoomType.Studio);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);
        Rent rent = new Rent(startDate, endDate, guest, room);
        Rent retrievedRent = rentRepository.getById(rent.getId());
        assertEquals(rent.getId(), retrievedRent.getId()); //make sure there was such a guest

        rentRepository.delete(retrievedRent);
        Rent deletedRent = rentRepository.getById(rent.getId());
        assertNull(deletedRent);
    }
}
