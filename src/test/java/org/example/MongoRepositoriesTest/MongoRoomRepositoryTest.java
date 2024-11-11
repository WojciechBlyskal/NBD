package org.example.MongoRepositoriesTest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.*;
import org.bson.conversions.Bson;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RoomMgd;
import org.example.MongoRepositories.ConnectionManager;
import org.example.MongoRepositories.GuestRepository;
import org.example.MongoRepositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MongoRoomRepositoryTest {
    private MicroSuiteMgd testRoom;

    @BeforeEach
    public void setUp(){
        testRoom = new MicroSuiteMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                12,
                3,
                37.5,
                200.0,
                0
        );
    }

    @Test
    public void addFindTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
            MongoCollection<RoomMgd> collection = connectionManager.getMongoDB().getCollection("roomCollection", RoomMgd.class);

            //comparing number of documents at before and after adding document
            long initialCount = collection.countDocuments();
            testMongoRoomRepository.addRemote(testRoom);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //checking if added document is the one we expected and find method
            Bson filter1 = Filters.and(
                    Filters.eq("number", 12),
                    Filters.eq("floor", 3),
                    Filters.eq("surface", 37.5),
                    Filters.eq("price", 200.0));
            ArrayList<RoomMgd> foundRooms = testMongoRoomRepository.findRemote(filter1);
            assertEquals(testRoom.getNumber(), foundRooms.getFirst().getNumber(), "Retrieved document does not match the added document");
            testMongoRoomRepository.dropCollection();
        }
    }

    @Test
    public void updateTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);
            testMongoRoomRepository.addRemote(testRoom);

            Bson update1 = Updates.set("number", 18);
            Bson update2 = Updates.set("floor", 4);
            Bson update4 = Updates.set("surface", 45.0);
            Bson filter1 = Filters.eq("price", 200.0);

            testMongoRoomRepository.updateRemote(filter1, update1);
            testMongoRoomRepository.updateRemote(filter1, update2);
            testMongoRoomRepository.updateRemote(filter1, update4);
            //MicroSuiteMgd room = (MicroSuiteMgd) testMongoRoomRepository.findRemote(filter1).getFirst();
            ArrayList<RoomMgd> room;
            room = testMongoRoomRepository.findRemote(filter1);
            assertEquals(18, room.getFirst().getNumber());
            assertEquals(4, room.getFirst().getFloor());
            assertEquals(45.0, room.getFirst().getSurface());

            Bson update3 = Updates.set("price", 150.0);
            Bson filter2 = Filters.eq("number", 18);
            testMongoRoomRepository.updateRemote(filter2, update3);

            room = testMongoRoomRepository.findRemote(filter2);
            assertEquals(150.0, room.getFirst().getPrice());

            testMongoRoomRepository.dropCollection();
        }
    }


    @Test
    public void removeTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);

            //providing and checking a document to remove
            MongoCollection<RoomMgd> collection = connectionManager.getMongoDB().getCollection("roomCollection", RoomMgd.class);
            long initialCount = collection.countDocuments();
            testMongoRoomRepository.addRemote(testRoom);
            long postAddCount = collection.countDocuments();
            assertEquals(initialCount + 1, postAddCount, "Document was not added successfully");

            //actual remove testing
            Bson filter1 = Filters.and(
                    Filters.eq("number", 12),
                    Filters.eq("floor", 3),
                    Filters.eq("surface", 37.5),
                    Filters.eq("price", 200.0));
            testMongoRoomRepository.removeRemote(filter1);
            long postRemoveCount = collection.countDocuments();
            assertEquals(initialCount, postRemoveCount, "Document was not removed successfully");

            testMongoRoomRepository.dropCollection();
        }
    }

    @Test
    public void testDropCollection() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RoomRepository testMongoClientRepository = new RoomRepository(connectionManager);
            testMongoClientRepository.addRemote(testRoom);

            RoomMgd testRoom2 = new MicroSuiteMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    12,
                    3,
                    37.5,
                    200.0,
                    0
            );

            testMongoClientRepository.addRemote(testRoom2);

            testMongoClientRepository.dropCollection();

            ArrayList<RoomMgd> allGuests = testMongoClientRepository.findRemote(Filters.empty());
            assertTrue(allGuests.isEmpty());
        }
    }

    @Test
    public void testRoomToBsonDocument() {
        BsonDocument document = new BsonDocument();
        document.append("number", new BsonInt32(testRoom.getNumber()));
        document.append("floor", new BsonInt32(testRoom.getFloor()));
        document.append("surface", new BsonDouble(testRoom.getSurface()));
        document.append("price", new BsonDouble(testRoom.getPrice()));
        document.append("_id", new BsonString(testRoom.getEntityId().toString()));

        assertEquals(12, document.getInt32("number").getValue());
        assertEquals(3, document.getInt32("floor").getValue());
        assertEquals(37.5, document.getDouble("surface").getValue());
        assertEquals(200.0, document.getDouble("price").getValue());
    }

    @Test
    public void testBsonDocumentToRoom() {
        Document document = new Document("_id", 7)
                .append("number", 8)
                .append("floor", 3)
                .append("surface", 25.0)
                .append("price", 100.0)
                .append("rented", 0);

        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000007");
        UniqueIdMgd entityId = new UniqueIdMgd(uuid);
        MicroSuiteMgd room = new MicroSuiteMgd(entityId,
                document.getInteger("number"),
                document.getInteger("floor"),
                document.getDouble("surface"),
                document.getDouble("price"),
                document.getInteger("rented"));
        assertEquals(8, room.getNumber());
        assertEquals(3, room.getFloor());
        assertEquals(25.0, room.getSurface());
        assertEquals(100.0, room.getPrice());
    }

    @Test
    public void testDataConsistencyAfterConversion() {
        Document document = new Document("number", testRoom.getNumber())
                .append("floor", testRoom.getFloor())
                .append("surface", testRoom.getSurface())
                .append("price", testRoom.getPrice())
                .append("rented", testRoom.getRented());

        UniqueIdMgd entityId = new UniqueIdMgd(UUID.fromString("00000000-0000-0000-0000-000000000008"));
        MicroSuiteMgd convertedRoom = new MicroSuiteMgd(entityId,
                document.getInteger("number"),
                document.getInteger("floor"),
                document.getDouble("surface"),
                document.getDouble("price"),
                document.getInteger("rented"));

        assertEquals(testRoom.getNumber(), convertedRoom.getNumber());
        assertEquals(testRoom.getFloor(), convertedRoom.getFloor());
        assertEquals(testRoom.getSurface(), convertedRoom.getSurface());
        assertEquals(testRoom.getPrice(), convertedRoom.getPrice());
    }
}
