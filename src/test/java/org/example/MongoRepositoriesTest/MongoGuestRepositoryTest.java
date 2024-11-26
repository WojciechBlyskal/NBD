package org.example.MongoRepositoriesTest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.Document;
import org.example.Mgd.GuestMgd;
import org.example.mongoRepositories.ConnectionManager;
import org.example.mongoRepositories.GuestRepository;
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

    @Test
    public void testDropCollection() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);
            testMongoClientRepository.addRemote(testGuest);

            GuestMgd guest2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    1235,
                    "Jan",
                    "Kowalski",
                    "123456789"
            );

            testMongoClientRepository.addRemote(guest2);

            testMongoClientRepository.dropCollection();

            ArrayList<GuestMgd> allGuests = testMongoClientRepository.findRemote(Filters.empty());
            assertTrue(allGuests.isEmpty());
        }
    }

    @Test
    public void testGuestToBsonDocument() {
        BsonDocument document = new BsonDocument();
        document.append("id", new BsonInt64(testGuest.getId()));
        document.append("name", new BsonString(testGuest.getName()));
        document.append("lastName", new BsonString(testGuest.getLastName()));
        document.append("phoneNumber", new BsonString(testGuest.getPhoneNumber()));
        document.append("_id", new BsonString(testGuest.getEntityId().toString()));

        assertEquals(1234, document.getInt64("id").getValue());
        assertEquals("Adam", document.getString("name").getValue());
        assertEquals("Kowalski", document.getString("lastName").getValue());
        assertEquals("123456789", document.getString("phoneNumber").getValue());
    }

    @Test
    public void testBsonDocumentToGuest() {
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

    /*@Test
    public void testClusterCrudOperationsWithMajorityNodesActive() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoClientRepository = new GuestRepository(connectionManager);
            //Guest guest = new Guest("Mark", "Williams", 10, "999888777");
            testMongoClientRepository.addRemote(testGuest);

            // Odczytaj dokument i sprawdź, czy dane są poprawne
            Guest retrievedGuest = (Guest) testMongoClientRepository.findRemote(guest.getId());
            assertNotNull(retrievedGuest);
            assertEquals("Mark", retrievedGuest.getName());
            assertEquals("Williams", retrievedGuest.getLastName());

            // Zaktualizuj dokument
            Bson update = Updates.set("phoneNumber", "000111222");
            testMongoClientRepository.updateRemote(Filters.eq("_id", guest.getId()), update);

            // Sprawdź, czy dane zostały poprawnie zaktualizowane
            Guest updatedGuest = (Guest) testMongoClientRepository.findRemote(guest.getId());
            assertEquals("000111222", updatedGuest.getPhoneNumber());

            // Usuń dokument
            testMongoClientRepository.removeRemote(guest.getId());

            // Sprawdź, czy dokument został usunięty
            Guest deletedGuest = (Guest) testMongoClientRepository.findRemote(guest.getId());
            assertNull(deletedGuest);
        }
    }*/
    /*{@Test
    public void testFailoverAndCrudOperations() throws InterruptedException {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            GuestRepository testMongoGuestRepository = new GuestRepository(connectionManager);
            // 1. Dodaj dokument, aby upewnić się, że klaster działa
            //Guest guest = new Guest("Tom", "Jackson", 11, "123123123");
            testMongoGuestRepository.addRemote(testGuest);

            // 2. Wyłącz węzeł primary (zakładając, że masz sposób na to w testach)
            clusterManager.shutdownPrimaryNode();

            // 3. Poczekaj, aż węzeł secondary stanie się primary
            Thread.sleep(5000); // Oczekiwanie na przełączenie - może być inny sposób

            // 4. Sprawdź, czy operacje CRUD nadal działają
            Bson filter1 = Filters.and(
                    Filters.eq("id", 1234),
                    Filters.eq("name", "Adam"),
                    Filters.eq("lastName", "Kowalski"),
                    Filters.eq("phoneNumber", "123456789"));
            ArrayList<GuestMgd> foundGuests = testMongoGuestRepository.findRemote(filter1);
            //GuestMgd retrievedGuest = (GuestMgd) testMongoGuestRepository.findRemote(testGuest.getId());
            assertNotNull(foundGuests);
            assertEquals("Adam", foundGuests.getFirst().getName());

            // Zaktualizuj dokument
            Bson update = Updates.set("phoneNumber", "321321321");
            testMongoGuestRepository.updateRemote(Filters.eq("_id", testGuest.getId()), update);

            // Sprawdź aktualizację
            GuestMgd updatedGuest = (GuestMgd) testMongoGuestRepository.findRemote(testGuest.getId());
            assertEquals("321321321", updatedGuest.getPhoneNumber());

            // Usuń dokument
            testMongoGuestRepository.removeRemote(testGuest.getId());
            GuestMgd deletedGuest = (GuestMgd) testMongoGuestRepository.findRemote(testGuest.getId());
            assertNull(deletedGuest);
        }
    }*/

}
