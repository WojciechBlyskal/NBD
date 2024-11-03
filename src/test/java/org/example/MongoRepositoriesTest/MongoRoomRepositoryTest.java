/*package org.example.MongoRepositoriesTest;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import simpleMgdTypes.BoolMgd;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MongoRoomRepositoryTest {
    private InterCityMgd testTrain;

    @BeforeEach
    public void setUp(){
        testTrain = new InterCityMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                "Tomek",
                10,
                5,
                4,
                100,
                new BoolMgd(false)
        );
    }

    @Test
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
    }
}*/
