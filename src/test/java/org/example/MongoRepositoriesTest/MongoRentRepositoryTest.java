package org.example.MongoRepositoriesTest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RentMgd;
import org.example.Mgd.RoomMgd;
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

import static org.junit.jupiter.api.Assertions.*;

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
                LocalDateTime.of(2024, 10, 22, 14, 30, 45),
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
                    Filters.eq("startTime", LocalDateTime.of(2024, 10, 15, 14, 30, 45)));
            ArrayList<RentMgd> foundRents = testMongoRentRepository.findRemote(filter1);
            assertEquals(testRent.getRentNumber(), foundRents.getFirst().getRentNumber(), "Retrieved document does not match the added document");
            testMongoRentRepository.dropCollection();
        }
    }

    @Test
    public void updateTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);
            testMongoRentRepository.addRemote(testRent);

            Bson update1 = Updates.set("rentNumber", "B52");
            Bson filter1 = Filters.eq("startTime", LocalDateTime.of(2024, 10, 15, 14, 30, 45));

            testMongoRentRepository.updateRemote(filter1, update1);

            ArrayList<RentMgd> foundRents = testMongoRentRepository.findRemote(filter1);

            //checking if it is not allowed to change properties that should not be allowed to be changed
            assertEquals("B52", foundRents.getFirst().getRentNumber());

            Bson update2 = Updates.set("startTime", LocalDateTime.of(2024, 10, 18, 14, 30, 45));
            Bson filter2 = Filters.eq("rentNumber", "B52");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                testMongoRentRepository.updateRemote(filter2, update2);
            });
            assertEquals("Updating startTime, endTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

            Bson update3 = Updates.set("endTime", LocalDateTime.of(2024, 10, 18, 14, 30, 45));
            exception = assertThrows(IllegalArgumentException.class, () -> {
                testMongoRentRepository.updateRemote(filter2, update3);
            });
            assertEquals("Updating startTime, endTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

            GuestMgd testGuest2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    4321,
                    "Piotr",
                    "Nowak",
                    "11122333"
            );

            Bson update4 = Updates.set("guestMgd", testGuest2);
            exception = assertThrows(IllegalArgumentException.class, () -> {
                testMongoRentRepository.updateRemote(filter2, update4);
            });
            assertEquals("Updating startTime, endTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

            MicroSuiteMgd testRoom2 = new MicroSuiteMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    2005,
                    4,
                    2.0,
                    2137.0
            );

            Bson update5 = Updates.set("roomMgd", testRoom2);
            exception = assertThrows(IllegalArgumentException.class, () -> {
                testMongoRentRepository.updateRemote(filter2, update5);
            });
            assertEquals("Updating startTime, endTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

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
            testMongoRentRepository.addRemote(testRent);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //actual remove testing
            Bson filter1 = Filters.and(
                    Filters.eq("rentNumber", "A34"),
                    Filters.eq("startTime", LocalDateTime.of(2024, 10, 15, 14, 30, 45)),
                    Filters.eq("endTime", LocalDateTime.of(2024, 10, 22, 14, 30, 45)));
            testMongoRentRepository.removeRemote(filter1);
            long postRemoveCount = collection.countDocuments();
            assertEquals(initialCount, postRemoveCount, "Document was not removed successfully");

            testMongoRentRepository.dropCollection();
        }
    }
}
