package org.example;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import org.example.cassandra.CassandraRepository;
import org.example.model.Guest;
import org.example.repositories.EntityRepository;
import org.example.repositories.GuestRepository;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuestRepositoryTest {
    private EntityRepository<Guest> guestRepository;
    protected CassandraRepository cassandraRepository;
    protected CqlSession session;

    @BeforeEach
    public void setUp() {
        cassandraRepository = new CassandraRepository();
        session = cassandraRepository.getSession();
        guestRepository = new GuestRepository(session, CqlIdentifier.fromCql("site"));
        session.execute("TRUNCATE site.guests;"); //getAllTest nie przechodzi bez tego
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
        guestRepository.create(guest);

        Guest retrievedGuest = guestRepository.getById(guest.getId());
        assertEquals(guest.getId(), retrievedGuest.getId());
    }

    @Test
    void getAllTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        guestRepository.create(guest);
        Guest guest2 = new Guest("Wojciech", "Laksylb", "123123123");
        guestRepository.create(guest2);

        List<Guest> retrievedGuests = guestRepository.getAll();
        assertEquals(2, retrievedGuests.size());
        assertTrue(retrievedGuests.stream().anyMatch(g -> g.getId().equals(guest.getId())));
        assertTrue(retrievedGuests.stream().anyMatch(g -> g.getId().equals(guest2.getId())));
    }

    @Test
    void updateTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        guestRepository.create(guest);
        Guest retrievedGuest = guestRepository.getById(guest.getId());
        assertEquals(guest.getId(), retrievedGuest.getId()); //make sure there was such a guest
        assertEquals("Jan", retrievedGuest.getName());
        assertEquals("Kowalski", retrievedGuest.getLastName());
        assertEquals("123456789", retrievedGuest.getPhoneNumber());

        guest.setName("Piotr"); //actual update test
        guest.setLastName("Nowak");
        guest.setPhoneNumber("987654321");
        guestRepository.update(guest);
        Guest updatedGuest = guestRepository.getById(guest.getId());
        assertEquals("Piotr", updatedGuest.getName());
        assertEquals("Nowak", updatedGuest.getLastName());
        assertEquals("987654321", updatedGuest.getPhoneNumber());
    }

    @Test
    void deleteTest() {
        Guest guest = new Guest("Jan", "Kowalski", "123456789");
        guestRepository.create(guest);
        Guest retrievedGuest = guestRepository.getById(guest.getId());
        assertEquals(guest.getId(), retrievedGuest.getId()); //make sure there was such a guest

        guestRepository.delete(retrievedGuest);
        Guest deletedGuest = guestRepository.getById(guest.getId());
        assertNull(deletedGuest);
    }
}
