package org.example.MongoRepositoriesTest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RentMgd;
import org.example.MongoRepositories.ConnectionManager;
import org.example.MongoRepositories.GuestRepository;
import org.example.MongoRepositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.simpleMgdTypes.BoolMgd;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MongoRentRepositoryTest {

    private GuestMgd testGuest;
    private MicroSuiteMgd testRoom;
    private RentMgd testRent;

    @BeforeEach
    public void setUp() throws Exception {
        testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                1234,
                "Adam",
                "Kowalski",
                "123456789"
        );

        testRoom = new MicroSuiteMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                12,
                3,
                37.5,
                200.0
        );

        testRent = new RentMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                "A34",
                LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                400.0,
                testGuest,
                testRoom
        );
    }

    @Test
    public void addFindTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);
            MongoCollection<RentMgd> collection = connectionManager.getMongoDB().getCollection("rentCollection", RentMgd.class);

            //comparing number of documents at before and after adding document
            long initialCount = collection.countDocuments();
            testMongoRentRepository.addRemote(testRent);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //checking if added document is the one we expected and find method
            Bson filter1 = Filters.and(
                    Filters.eq("rentNumber", "A34"),
                    Filters.eq("startTime", LocalDateTime.of(2024, 10, 15, 14, 30, 45)),
                    Filters.eq("cost", 400.0));
            ArrayList<RentMgd> foundRents = testMongoRentRepository.findRemote(filter1);
            assertEquals(testRent.getRentNumber(), foundRents.getFirst().getRentNumber(), "Retrieved document does not match the added document");
            testMongoRentRepository.dropCollection();
        }
    }

    /*@Test
    public void updateTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);
            testMongoRentRepository.addRemote(testGuest);

            Bson update1 = Updates.set("roomNumber", "B52");
            Bson update2 = Updates.set("cost", 500.0);
            Bson filter1 = Filters.eq("lastName", "Kowalski");

            testMongoRentRepository.updateRemote(filter1, update1);
            testMongoRentRepository.updateRemote(filter1, update2);
            testMongoRentRepository.updateRemote(filter1, update4);
            GuestMgd guest = testMongoRentRepository.findRemote(filter1).getFirst();

            assertEquals(1500, guest.getId());
            assertEquals("Piotr", guest.getName());
            assertEquals("111222333", guest.getPhoneNumber());

            Bson update3 = Updates.set("lastName", "Kot");
            Bson filter2 = Filters.eq("name", "Piotr");
            testMongoRentRepository.updateRemote(filter2, update3);

            guest = testMongoRentRepository.findRemote(filter2).getFirst();
            assertEquals("Kot", guest.getLastName());

            testMongoRentRepository.dropCollection();
        }
    }

    @Test
    public void removeTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);

            //providing and checking a document to remove
            MongoCollection<RentMgd> collection = connectionManager.getMongoDB().getCollection("rentCollection", RentMgd.class);
            long initialCount = collection.countDocuments();
            testMongoRentRepository.addRemote(testGuest);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //actual remove testing
            Bson filter1 = Filters.and(
                    Filters.eq("id", 1234),
                    Filters.eq("name", "Adam"),
                    Filters.eq("lastName", "Kowalski"),
                    Filters.eq("phoneNumber", "123456789"));
            testMongoRentRepository.removeRemote(filter1);
            long postRemoveCount = collection.countDocuments();
            assertEquals(initialCount, postRemoveCount, "Document was not removed successfully");

            testMongoRentRepository.dropCollection();
        }
    }*/
}
