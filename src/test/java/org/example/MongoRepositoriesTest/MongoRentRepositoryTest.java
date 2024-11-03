/*package org.example.MongoRepositoriesTest;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.*;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simpleMgdTypes.BoolMgd;
import simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MongoRentRepositoryTest {

    private ClientMgd testClient;
    private InterCityMgd testTrain;
    private ReservationMgd testReservation;

    @BeforeEach
    public void setUp() throws Exception {
        testClient = new ClientMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                "Adam",
                "Kowalski",
                1000
        );

        testTrain = new InterCityMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                "Tomek",
                1,
                5,
                2,
                100,
                new BoolMgd(false)
        );

        testReservation = new ReservationMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                10,
                5,
                testClient,
                testTrain
        );
    }

    @Test
    public void RemoteRepositoryTest() throws Exception {
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoReservationRepository testMongoReservationRepository = new MongoReservationRepository(connectionManager);
            MongoClientRepository testMongoClientRepository = new MongoClientRepository(connectionManager);
            MongoTrainRepository testMongoTrainRepository = new MongoTrainRepository(connectionManager);

            //Create and Find Tests
            testMongoReservationRepository.addToRemoteRepository(testReservation);

            ArrayList<ReservationMgd> foundReservations;
            Bson filter1 = Filters.and(
                    Filters.eq("seatnumber", 10),
                    Filters.eq("triplengthbystations", 5));

            foundReservations = testMongoReservationRepository.findOnRemoteRepository(filter1);

            //Assertions of all the attributes
            assertEquals(1,
                    foundReservations.size());
            assertEquals(10,
                    foundReservations.get(0).getSeatNumber());
            assertEquals(5,
                    foundReservations.get(0).getTripLengthByStations());
            assertEquals(testClient.getEntityId().getUuid(),
                    foundReservations.get(0).getClient().getEntityId().getUuid());
            assertEquals(testTrain.getEntityId().getUuid(),
                    foundReservations.get(0).getTrain().getEntityId().getUuid());


            //Update Tests
            Bson update = Updates.set("seatnumber", 20L);

            Bson filterUpdate = Filters.and(
                    Filters.eq("seatnumber", 20),
                    Filters.eq("triplengthbystations", 5));
            testMongoReservationRepository.updateRemoteRepository(filter1,
                    update);
            foundReservations = testMongoReservationRepository.findOnRemoteRepository(filterUpdate);

            assertEquals(20,
                    foundReservations.get(0).getSeatNumber());


            //More than one element tests
            ReservationMgd testReservation2 = new ReservationMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    20,
                    10,
                    testClient,
                    testTrain
            );

            Bson filter2 = Filters.and(
                    Filters.eq("seatnumber", 20));

            //Finding two reservations
            testMongoReservationRepository.addToRemoteRepository(testReservation2);
            foundReservations = testMongoReservationRepository.findOnRemoteRepository(filter2);

            assertEquals(2,
                    foundReservations.size());
            assertEquals(20,
                    foundReservations.get(0).getSeatNumber());
            assertEquals(20,
                    foundReservations.get(1).getSeatNumber());

            //Assert train limits
            boolean exeptionChecked = false;
            ReservationMgd testReservation3 = new ReservationMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    21,
                    10,
                    testClient,
                    testTrain);
            try {
                testMongoReservationRepository.addToRemoteRepository(testReservation3);
            } catch (Exception e){
                if(e.getMessage() ==
                        "Cannot reserve seat - there are no free seats"){
                    exeptionChecked = true;
                }
            }

            assertTrue(exeptionChecked);

            Bson reservation3Filter = Filters.eq("_id", testReservation3.getEntityId().getUuid());
            testMongoReservationRepository.removeFromRemoteRepository(reservation3Filter);

            //Find embedded objects in all reservations
            Bson clientFilter = Filters.eq("firstname", "Adam");
            Bson trainFilter = Filters.eq("trainname", "Tomek");

            ArrayList<ClientMgd> foundClients =
                    testMongoClientRepository.findOnRemoteRepositoryInAllReservations(clientFilter);
            assertEquals(2,
                    foundClients.size());
            assertEquals("Adam",
                    foundClients.get(0).getFirstName());
            assertEquals("Adam",
                    foundClients.get(1).getFirstName());

            ArrayList<TrainMgd> foundTrains =
                    testMongoTrainRepository.findOnRemoteRepositoryInAllReservations(trainFilter);
            assertEquals(2,
                    foundTrains.size());
            assertEquals("Tomek",
                    foundTrains.get(0).getName());
            assertEquals("Tomek",
                    foundTrains.get(1).getName());


            //Remove tests
            Bson firstReservationfilter = Filters.eq("_id", testReservation.getEntityId().getUuid());
            testMongoReservationRepository.removeFromRemoteRepository(firstReservationfilter);
            foundReservations = testMongoReservationRepository.findOnRemoteRepository(filter2);

            assertEquals(1,
                    foundReservations.size());
            assertEquals(10,
                    foundReservations.get(0).getTripLengthByStations());

            testMongoReservationRepository.removeFromRemoteRepository(filter2);
            foundReservations = testMongoReservationRepository.findOnRemoteRepository(filter2);

            assertEquals(0,
                    foundReservations.size());

            Bson removeClientFilter = Filters.eq("_id", testClient.getEntityId().getUuid());
            testMongoClientRepository.removeFromRemoteRepository(removeClientFilter);
            Bson removeTrainFilter = Filters.eq("_id", testTrain.getEntityId().getUuid());
            testMongoTrainRepository.removeFromRemoteRepository(removeTrainFilter);

            testMongoClientRepository.dropCollection();
            testMongoTrainRepository.dropCollection();
            testMongoReservationRepository.dropCollection();
        }
    }

    @Test
    public void addToRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoReservationRepository testMongoReservationRepository = new MongoReservationRepository(connectionManager);

            testMongoReservationRepository.addToLocalRepository(testReservation);
            assertEquals(testMongoReservationRepository.getLocalRepository().size(),
                    1);

            testMongoReservationRepository.addToLocalRepository(testReservation);
            assertEquals(testMongoReservationRepository.getLocalRepository().size(),
                    2);
            assertEquals(testMongoReservationRepository.getLocalRepository().get(1),
                    testReservation);

            testMongoReservationRepository.dropCollection();
        }
    }

    @Test
    public void removeFromRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoReservationRepository testMongoReservationRepository = new MongoReservationRepository(connectionManager);

            testMongoReservationRepository.addToLocalRepository(testReservation);
            assertEquals(testMongoReservationRepository.getLocalRepository().size(),
                    1);

            testMongoReservationRepository.removeFromLocalRepository(testReservation);
            assertEquals(testMongoReservationRepository.getLocalRepository().size(),
                    0);

            testMongoReservationRepository.dropCollection();
        }
    }

    @Test
    public void getRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoReservationRepository testMongoReservationRepository = new MongoReservationRepository(connectionManager);

            testMongoReservationRepository.addToLocalRepository(testReservation);
            assertEquals(testMongoReservationRepository.getLocalRepository().get(0),
                    testReservation);

            testMongoReservationRepository.dropCollection();
        }
    }

    @Test
    public void clearRepositoryTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoReservationRepository testMongoReservationRepository = new MongoReservationRepository(connectionManager);

            testMongoReservationRepository.addToLocalRepository(testReservation);
            testMongoReservationRepository.addToLocalRepository(testReservation);
            assertEquals(testMongoReservationRepository.getLocalRepository().size(),
                    2);
            testMongoReservationRepository.clearLocalRepository();
            assertEquals(testMongoReservationRepository.getLocalRepository().size(),
                    0);

            testMongoReservationRepository.dropCollection();
        }
    }

    @Test
    public void creatingCollectionsTest(){
        try (ConnectionManager connectionManager = new ConnectionManager()) {

            MongoReservationRepository testMongoReservationRepository = new MongoReservationRepository(connectionManager);
            testMongoReservationRepository.dropCollection();

            MongoReservationRepository testMongoReservationRepository1 = new MongoReservationRepository(connectionManager);

            MongoReservationRepository testMongoReservationRepository2 = new MongoReservationRepository(connectionManager);

            testMongoReservationRepository1.dropCollection();
        }
    }
}*/
