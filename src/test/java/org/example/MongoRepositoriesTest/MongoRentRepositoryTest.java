package org.example.MongoRepositoriesTest;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.*;
import org.bson.conversions.Bson;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RentMgd;
import org.example.Mgd.RoomMgd;
import org.example.mongoRepositories.ConnectionManager;
import org.example.mongoRepositories.GuestRepository;
import org.example.mongoRepositories.RentRepository;
import org.example.mongoRepositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
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
                200.0,
                0
        );

        testRent = new RentMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                "A34",
                LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                testGuest,
                testRoom
        );
    }

    @Test
    public void addFindTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoGuestRepository = new GuestRepository(connectionManager);
            RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            //RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
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
            assertEquals("Updating startTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

            Bson update3 = Updates.set("endTime", LocalDateTime.of(2024, 10, 18, 14, 30, 45));
            testMongoRentRepository.updateRemote(filter2, update3);
            foundRents = testMongoRentRepository.findRemote(filter2);
            assertEquals(LocalDateTime.of(2024, 10, 18, 14, 30, 45), foundRents.getFirst().getEndTime());

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
            assertEquals("Updating startTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

            MicroSuiteMgd testRoom2 = new MicroSuiteMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    2005,
                    4,
                    2.0,
                    2137.0,
                    0
            );

            Bson update5 = Updates.set("roomMgd", testRoom2);
            exception = assertThrows(IllegalArgumentException.class, () -> {
                testMongoRentRepository.updateRemote(filter2, update5);
            });
            assertEquals("Updating startTime, 'roomMgd', 'guestMgd' fields is not allowed.", exception.getMessage());

            testMongoRentRepository.dropCollection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void removeTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            //RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
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
                    Filters.eq("startTime", LocalDateTime.of(2024, 10, 15, 14, 30, 45)));
            testMongoRentRepository.removeRemote(filter1);
            long postRemoveCount = collection.countDocuments();
            assertEquals(initialCount, postRemoveCount, "Document was removed, despite it should not be");

            testMongoRentRepository.dropCollection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void concurrentTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);
            GuestMgd testGuest2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    1500,
                    "Jan",
                    "Nowak",
                    "567898754"
            );

            RentMgd testRent2 = new RentMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    "A35",
                    LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                    testGuest2,
                    testRoom
            );
            assertEquals(0,testRoom.getRented());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDropCollection() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            //RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);
            testMongoRentRepository.addRemote(testRent);

            GuestMgd guest2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    1235,
                    "Jan",
                    "Kowalski",
                    "123456789"
            );

            RoomMgd testRoom2 = new MicroSuiteMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    12,
                    3,
                    37.5,
                    200.0,
                    0
            );

            RentMgd rent2 = new RentMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    "A52",
                    LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                    guest2,
                    testRoom2
            );

            testMongoRentRepository.addRemote(rent2);

            testMongoRentRepository.dropCollection();

            ArrayList<RentMgd> allRents = testMongoRentRepository.findRemote(Filters.empty());
            assertTrue(allRents.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRentToBsonDocument() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            //RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
            RentRepository testMongoRentRepository = new RentRepository(connectionManager);
            LocalDateTime startTime = testRent.getStartTime();
            Date date1 = Date.from(startTime.toInstant(ZoneOffset.UTC));

            BsonDocument document = new BsonDocument();
            document.append("rentNumber", new BsonString(testRent.getRentNumber()));
            document.append("startTime", new BsonDateTime(date1.getTime()));
            BsonDocument guestDocument = new BsonDocument();
            BsonDocument roomDocument = new BsonDocument();

            document.append("id", new BsonInt64(testGuest.getId()));
            document.append("name", new BsonString(testGuest.getName()));
            document.append("lastName", new BsonString(testGuest.getLastName()));
            document.append("phoneNumber", new BsonString(testGuest.getPhoneNumber()));
            document.append("_id", new BsonString(testGuest.getEntityId().toString()));
            document.append("guest", guestDocument);

            document.append("number", new BsonInt32(testRoom.getNumber()));
            document.append("floor", new BsonInt32(testRoom.getFloor()));
            document.append("surface", new BsonDouble(testRoom.getSurface()));
            document.append("price", new BsonDouble(testRoom.getPrice()));
            document.append("_id", new BsonString(testRoom.getEntityId().toString()));
            document.append("room", roomDocument);

            assertEquals("A34", document.getString("rentNumber").getValue());
            assertEquals(document.getDateTime("startTime").getValue(),
                    document.getDateTime("startTime").getValue());

        }
    }

    @Test
    public void testBsonDocumentToRent() {
        Document document = new Document("_id", 7)
                .append("id", 8)
                .append("name", "Jan")
                .append("lastName", "Nowak")
                .append("phoneNumber", "888999000");

        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000007");
        UniqueIdMgd entityId = new UniqueIdMgd(uuid);
        GuestMgd guest = new GuestMgd(entityId,
                document.getInteger("id"),
                document.getString("name"),
                document.getString("lastName"),
                document.getString("phoneNumber"));
        assertEquals(8, guest.getId());
        assertEquals("Jan", guest.getName());
        assertEquals("Nowak", guest.getLastName());
        assertEquals("888999000", guest.getPhoneNumber());
    }

    @Test
    public void testDataConsistencyAfterConversion() {
        Document document = new Document("id", 1234)
                .append("name", testGuest.getName())
                .append("lastName", testGuest.getLastName())
                .append("phoneNumber", testGuest.getPhoneNumber());

        UniqueIdMgd entityId = new UniqueIdMgd(UUID.fromString("00000000-0000-0000-0000-000000000008"));
        GuestMgd convertedGuest = new GuestMgd(entityId,
                document.getInteger("id"),
                document.getString("name"),
                document.getString("lastName"),
                document.getString("phoneNumber"));
        assertEquals(testGuest.getId(), convertedGuest.getId());
        assertEquals(testGuest.getName(), convertedGuest.getName());
        assertEquals(testGuest.getLastName(), convertedGuest.getLastName());
        assertEquals(testGuest.getPhoneNumber(), convertedGuest.getPhoneNumber());
    }
}
