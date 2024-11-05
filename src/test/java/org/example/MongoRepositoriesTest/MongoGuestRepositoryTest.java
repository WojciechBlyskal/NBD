package org.example.MongoRepositoriesTest;

import com.mongodb.client.MongoCollection;
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
    public void addFindTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);
            MongoCollection<GuestMgd> collection = connectionManager.getMongoDB().getCollection("guestCollection", GuestMgd.class);

            //comparing number of documents at before and after adding document
            long initialCount = collection.countDocuments();
            testMongoClientRepository.addRemote(testGuest);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //checking if added document is the one we expected and find method
            Bson filter1 = Filters.and(
                    Filters.eq("id", 1234),
                    Filters.eq("name", "Adam"),
                    Filters.eq("lastName", "Kowalski"),
                    Filters.eq("phoneNumber", "123456789"));
            ArrayList<GuestMgd> foundGuests = testMongoClientRepository.findRemote(filter1);
            assertEquals(testGuest.getId(), foundGuests.getFirst().getId(), "Retrieved document does not match the added document");
            testMongoClientRepository.dropCollection();
        }
    }

    @Test
    public void updateTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);
            testMongoClientRepository.addRemote(testGuest);

            Bson update1 = Updates.set("id", 1500);
            Bson update2 = Updates.set("name", "Piotr");
            Bson update4 = Updates.set("phoneNumber", "111222333");
            Bson filter1 = Filters.eq("lastName", "Kowalski");

            testMongoClientRepository.updateRemote(filter1, update1);
            testMongoClientRepository.updateRemote(filter1, update2);
            testMongoClientRepository.updateRemote(filter1, update4);
            GuestMgd guest = testMongoClientRepository.findRemote(filter1).getFirst();

            assertEquals(1500, guest.getId());
            assertEquals("Piotr", guest.getName());
            assertEquals("111222333", guest.getPhoneNumber());

            Bson update3 = Updates.set("lastName", "Kot");
            Bson filter2 = Filters.eq("name", "Piotr");
            testMongoClientRepository.updateRemote(filter2, update3);

            guest = testMongoClientRepository.findRemote(filter2).getFirst();
            assertEquals("Kot", guest.getLastName());

            testMongoClientRepository.dropCollection();
        }
    }

    @Test
    public void removeTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);

            //providing and checking a document to remove
            MongoCollection<GuestMgd> collection = connectionManager.getMongoDB().getCollection("guestCollection", GuestMgd.class);
            long initialCount = collection.countDocuments();
            testMongoClientRepository.addRemote(testGuest);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //actual remove testing
            Bson filter1 = Filters.and(
                    Filters.eq("id", 1234),
                    Filters.eq("name", "Adam"),
                    Filters.eq("lastName", "Kowalski"),
                    Filters.eq("phoneNumber", "123456789"));
            testMongoClientRepository.removeRemote(filter1);
            long postRemoveCount = collection.countDocuments();
            assertEquals(initialCount, postRemoveCount, "Document was not removed successfully");

            testMongoClientRepository.dropCollection();
        }
    }

    /*@Test
    public void RemoteRepositoryTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);

            //Create and Find Tests
            testMongoClientRepository.addRemote(testGuest);

            ArrayList<GuestMgd> foundGuests;
            //Bson filter1 = Filters.eq("lastName", "Kowalski");
            Bson filter1 = Filters.eq("lastName", "Kowalski");
            foundGuests = testMongoClientRepository.findRemote(filter1);
            //Assertions of all the attributes
            assertEquals(1, foundGuests.size());
            assertEquals("Kowalski", foundGuests.getFirst().getLastName());
            assertEquals("123456789", foundGuests.getFirst().getPhoneNumber());

            //Update Tests
            Bson update = Updates.set("id", 1500);

            testMongoClientRepository.updateRemote(filter1,
                    update);
            foundGuests = testMongoClientRepository.findRemote(filter1);

            assertEquals(1500, foundGuests.getFirst().getId());


            GuestMgd testClient2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    4321,
                    "Jan",
                    "Nowak",
                    "987654321"
            );

            testMongoClientRepository.addRemote(testClient2);
            Bson filter2 = Filters.eq("lastName", "Nowak");
            foundGuests.add(testMongoClientRepository.findRemote(filter2).getFirst());

            assertEquals(2, foundGuests.size());
            assertEquals("Adam", foundGuests.get(0).getName());
            assertEquals("Jan", foundGuests.get(1).getName());
            testMongoClientRepository.dropCollection();
        }
    }*/
}
