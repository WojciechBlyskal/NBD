package org.example.MongoRepositoriesTest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RoomMgd;
import org.example.MongoRepositories.ConnectionManager;
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
                200.0
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

    /*@Test
    public void RemoteRepositoryTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {
            RoomRepository testMongoRoomRepository = new RoomRepository(connectionManager);

            //Create and Find Tests
            testMongoRoomRepository.addRemote(testRoom);

            ArrayList<GuestMgd> foundGuests;
            //Bson filter1 = Filters.eq("lastName", "Kowalski");
            Bson filter1 = Filters.eq("lastName", "Kowalski");
            foundGuests = testMongoRoomRepository.findRemote(filter1);
            //Assertions of all the attributes
            assertEquals(1, foundGuests.size());
            assertEquals("Kowalski", foundGuests.getFirst().getLastName());
            assertEquals("123456789", foundGuests.getFirst().getPhoneNumber());

            //Update Tests
            Bson update = Updates.set("id", 1500);

            testMongoRoomRepository.updateRemote(filter1,
                    update);
            foundGuests = testMongoRoomRepository.findRemote(filter1);

            assertEquals(1500, foundGuests.getFirst().getId());


            GuestMgd testClient2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    4321,
                    "Jan",
                    "Nowak",
                    "987654321"
            );

            testMongoRoomRepository.addRemote(testClient2);
            Bson filter2 = Filters.eq("lastName", "Nowak");
            foundGuests.add(testMongoRoomRepository.findRemote(filter2).getFirst());

            assertEquals(2, foundGuests.size());
            assertEquals("Adam", foundGuests.get(0).getName());
            assertEquals("Jan", foundGuests.get(1).getName());
            testMongoRoomRepository.dropCollection();
        }
    }*/

    /*@Test
    public void RemoteRepositoryTest() {
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);

            //Create and Find Tests
            testMongoTrainRepository.addToRemoteRepository(testTrain);

            ArrayList<TrainMgd> foundTrains;
            Bson filter1 = Filters.and(
                    Filters.eq("trainname", "Tomek"),
                    Filters.eq("amountofcars", 10));

            foundTrains = testMongoTrainRepository.findOnRemoteRepository(filter1);

            //Assertions of all the attributes
            assertEquals(1,
                    foundTrains.size());
            assertEquals("Tomek",
                    foundTrains.get(0).getName());
            assertEquals(10,
                    foundTrains.get(0).getAmountOfCars());
            assertEquals(5,
                    foundTrains.get(0).getAmountOfStations());
            assertEquals(4,
                    ((InterCityMgd)foundTrains.get(0)).getAmountOfSeatsPerCar());
            assertEquals(100,
                    ((InterCityMgd)foundTrains.get(0)).getMaxSpeed());
            assertEquals(false,
                    ((InterCityMgd)foundTrains.get(0)).getIsInternational().getABoolean());


            //Update Tests
            Bson update = Updates.set("amountofstations", 7);

            testMongoTrainRepository.updateRemoteRepository(filter1,
                    update);
            foundTrains = testMongoTrainRepository.findOnRemoteRepository(filter1);

            assertEquals(7,
                    foundTrains.get(0).getAmountOfStations());


            //More than one element tests
            TLKMgd testTrain2 = new TLKMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    "Tomek",
                    20,
                    5,
                    4,
                    100
            );
            Bson filter2 = Filters.and(
                    Filters.eq("trainname", "Tomek"));

            testMongoTrainRepository.addToRemoteRepository(testTrain2);
            foundTrains = testMongoTrainRepository.findOnRemoteRepository(filter2);

            assertEquals(2,
                    foundTrains.size());
            assertEquals("Tomek",
                    foundTrains.get(0).getName());
            assertEquals("Tomek",
                    foundTrains.get(1).getName());

            //Remove tests
            testMongoTrainRepository.removeFromRemoteRepository(filter1);
            foundTrains = testMongoTrainRepository.findOnRemoteRepository(filter2);

            assertEquals(1,
                    foundTrains.size());
            assertEquals(testTrain2.getAmountOfCars(),
                    foundTrains.get(0).getAmountOfCars());

            testMongoTrainRepository.removeFromRemoteRepository(filter2);
            foundTrains = testMongoTrainRepository.findOnRemoteRepository(filter2);

            assertEquals(0,
                    foundTrains.size());

            testMongoTrainRepository.dropCollection();
        }
    }

    @Test
    public void addToRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);

            testMongoTrainRepository.addToLocalRepository(testTrain);
            assertEquals(testMongoTrainRepository.getLocalRepository().size(),
                    1);

            testMongoTrainRepository.addToLocalRepository(testTrain);
            assertEquals(testMongoTrainRepository.getLocalRepository().size(),
                    2);
            assertEquals(testMongoTrainRepository.getLocalRepository().get(1),
                    testTrain);

            testMongoTrainRepository.dropCollection();
        }
    }

    @Test
    public void removeFromRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);

            testMongoTrainRepository.addToLocalRepository(testTrain);
            assertEquals(testMongoTrainRepository.getLocalRepository().size(),
                    1);

            testMongoTrainRepository.removeFromLocalRepository(testTrain);
            assertEquals(testMongoTrainRepository.getLocalRepository().size(),
                    0);

            testMongoTrainRepository.dropCollection();
        }
    }

    @Test
    public void getRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);

            testMongoTrainRepository.addToLocalRepository(testTrain);
            assertEquals(testMongoTrainRepository.getLocalRepository().get(0),
                    testTrain);

            testMongoTrainRepository.dropCollection();
        }
    }

    @Test
    public void clearRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);

            testMongoTrainRepository.addToLocalRepository(testTrain);
            testMongoTrainRepository.addToLocalRepository(testTrain);
            assertEquals(testMongoTrainRepository.getLocalRepository().size(),
                    2);
            testMongoTrainRepository.clearLocalRepository();
            assertEquals(testMongoTrainRepository.getLocalRepository().size(),
                    0);

            testMongoTrainRepository.dropCollection();
        }
    }

    @Test
    public void creatingCollectionsTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);
            testMongoTrainRepository.dropCollection();

            MongoTrainRepository testMongoTrainRepository1 = new MongoTrainRepository(connectionManager);

            MongoTrainRepository testMongoTrainRepository2 = new MongoTrainRepository(connectionManager);

            testMongoTrainRepository1.dropCollection();
        }
    }*/
}
