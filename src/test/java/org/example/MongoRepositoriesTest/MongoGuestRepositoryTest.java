package org.example.MongoRepositoriesTest;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.example.Mgd.GuestMgd;
import org.example.MongoRepositories.ConnectionManager;
import org.example.MongoRepositories.GuestRepository;
import org.example.simpleMgdTypes.UniqueIdMgd;

import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MongoGuestRepositoryTest {

    private GuestMgd testGuest;

    @BeforeEach
    public void setUp(){
        testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                1234,
                "Adam",
                "Kowalski",
                "123456789"
        );
    }

    @Test
    public void RemoteRepositoryTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);

            //Create and Find Tests
            testMongoClientRepository.addRemote(testGuest);

            ArrayList<GuestMgd> foundGuests;
            Bson filter1 = Filters.and(
                    Filters.eq("name", "Adam"),
                    Filters.eq("lastname", "Kowalski"));

            foundGuests = testMongoClientRepository.findRemote(filter1);

            //Assertions of all the attributes
            assertEquals(1, foundGuests.size());
            assertEquals(1234, foundGuests.getFirst().getId());
            assertEquals("Adam", foundGuests.getFirst().getName());
            assertEquals("Kowalski", foundGuests.getFirst().getLastName());
            assertEquals("123456789", foundGuests.getFirst().getPhoneNumber());

            //Update Tests
            Bson update = Updates.set("personalid", 1500);

            testMongoClientRepository.updateRemote(filter1,
                    update);
            foundGuests = testMongoClientRepository.findRemote(filter1);

            assertEquals(1500,
                    foundGuests.getFirst().getId());


            GuestMgd testClient2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    4321,
                    "Jan",
                    "Nowak",
                    "987654321"
            );
            Bson filter2 = Filters.and(
                    Filters.eq("name", "Jan"));

            testMongoClientRepository.addRemote(testClient2);
            foundGuests = testMongoClientRepository.findRemote(filter2);

            assertEquals(2,
                    foundGuests.size());
            assertEquals("Adam",
                    foundGuests.get(0).getName());
            assertEquals("Jan",
                    foundGuests.get(1).getName());

            //Find with limit tests
            foundGuests = testMongoClientRepository.findRemote(filter2);

            assertEquals(1, foundGuests.size());
            assertTrue(Objects.equals(foundGuests.getFirst().getLastName(), "Nowak")
                    || Objects.equals(foundGuests.getFirst().getLastName(), "Kowalski"));


            //Remove tests
            testMongoClientRepository.removeRemote(filter1);
            foundGuests = testMongoClientRepository.findRemote(filter2);

            assertEquals(1,
                    foundGuests.size());
            assertEquals(testClient2.getLastName(),
                    foundGuests.getFirst().getLastName());

            testMongoClientRepository.removeRemote(filter2);
            foundGuests = testMongoClientRepository.findRemote(filter2);

            assertEquals(0,
                    foundGuests.size());

            testMongoClientRepository.dropCollection();
        }
    }

    /*@Test
    public void addToRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoClientRepository testMongoClientRepository = new MongoClientRepository(connectionManager);

            testMongoClientRepository.addToLocalRepository(testClient);
            assertEquals(testMongoClientRepository.getLocalRepository().size(),
                    1);

            testMongoClientRepository.addToLocalRepository(testClient);
            assertEquals(testMongoClientRepository.getLocalRepository().size(),
                    2);
            assertEquals(testMongoClientRepository.getLocalRepository().get(1),
                    testClient);

            testMongoClientRepository.dropCollection();
        }
    }

    @Test
    public void removeFromRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoClientRepository testMongoClientRepository = new MongoClientRepository(connectionManager);

            testMongoClientRepository.addToLocalRepository(testClient);
            assertEquals(testMongoClientRepository.getLocalRepository().size(),
                    1);

            testMongoClientRepository.removeFromLocalRepository(testClient);
            assertEquals(testMongoClientRepository.getLocalRepository().size(),
                    0);

            testMongoClientRepository.dropCollection();
        }
    }

    @Test
    public void getRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoClientRepository testMongoClientRepository = new MongoClientRepository(connectionManager);

            testMongoClientRepository.addToLocalRepository(testClient);
            assertEquals(testMongoClientRepository.getLocalRepository().get(0),
                    testClient);

            testMongoClientRepository.dropCollection();
        }
    }

    @Test
    public void clearRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoClientRepository testMongoClientRepository = new MongoClientRepository(connectionManager);

            testMongoClientRepository.addToLocalRepository(testClient);
            testMongoClientRepository.addToLocalRepository(testClient);
            assertEquals(testMongoClientRepository.getLocalRepository().size(),
                    2);
            testMongoClientRepository.clearLocalRepository();
            assertEquals(testMongoClientRepository.getLocalRepository().size(),
                    0);

            testMongoClientRepository.dropCollection();
        }
    }

    @Test
    public void creatingCollectionsTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoClientRepository testMongoClientRepository = new MongoClientRepository(connectionManager);
            testMongoClientRepository.dropCollection();

            MongoClientRepository testMongoClientRepository1 = new MongoClientRepository(connectionManager);

            MongoClientRepository testMongoClientRepository2 = new MongoClientRepository(connectionManager);

            testMongoClientRepository1.dropCollection();
        }
    }*/
}
