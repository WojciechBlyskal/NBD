package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RentMgd;
import org.example.mongoRepositories.*;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.simpleMgdTypes.UniqueIdMgd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.redisRepositories.Decorators.RentDecorator;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisRentDecoratedRepositoryTest {

    private GuestMgd testGuest;
    private MicroSuiteMgd testRoom;
    private RentMgd testRent;

    @BeforeEach
    public void setUp(){

        testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                3000,
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
                "A15",
                LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                testGuest,
                testRoom
        );
    }

    @Test
    public void remoteRedisTest() throws JsonProcessingException {
        try (RedisRepository redisRepository
                     = new RedisRepository("redisConnectionFiles/workingCluster")){

            RedisDecoratedRepository testRentRedisDecoratedRepository =
                    new RedisDecoratedRepository(
                            new RentDecorator(
                                    redisRepository
                            )
                    );

            testRentRedisDecoratedRepository.addToCache(testRent);

            RentMgd foundRent = (RentMgd) testRentRedisDecoratedRepository.findInCache(testRent
                    .getEntityId());
            assertEquals(foundRent.getEntityId().getUuid(),
                    testRent.getEntityId().getUuid());

            testRentRedisDecoratedRepository.deleteFromCache(testRent
                    .getEntityId());

            boolean notFound = false;
            try {
                foundRent = (RentMgd) testRentRedisDecoratedRepository.findInCache(testRent
                        .getEntityId());
            } catch (NoSuchElementException noSuchElementException){
                notFound = true;
            }
            assertEquals (notFound,
                    true);
        }
    }

    @Test
    public void notConnectedTest() throws Exception {

        try (RedisRepository redisRepository
                     = new RedisRepository("redisConnectionFiles/notWorkingCluster");
             ConnectionManager connectionManager
                     = new ConnectionManager()) {

            MongoDecoratedRepository testRentMongoDecoratedRepository =
                    new MongoDecoratedRepository(
                            new RedisDecoratedRepository(
                                    new RentDecorator(
                                            redisRepository
                                    ),
                                    new RentRepository(connectionManager)
                            )
                    );

            testRentMongoDecoratedRepository
                    .addRemote(testRent);

            RentMgd foundRent =
                    (RentMgd) testRentMongoDecoratedRepository.findRemote(testRent
                            .getEntityId());
            assertEquals(foundRent.getEntityId().getUuid(),
                    testRent.getEntityId().getUuid());


            //Assert train limits
            boolean exeptionChecked = false;

            GuestMgd testGuest2 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    2000,
                    "Jan",
                    "Zielinski",
                    "123456789"
            );

            MicroSuiteMgd testRoom2 = new MicroSuiteMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    13,
                    3,
                    37.5,
                    200.0,
                    0
            );

            RentMgd testRent2 = new RentMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    "B14",
                    LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                    testGuest2,
                    testRoom2
            );

            testRentMongoDecoratedRepository.addRemote(testRent2);

            GuestMgd testGuest3 = new GuestMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    1000,
                    "Jan",
                    "Pawel",
                    "123456789"
            );

            MicroSuiteMgd testRoom3 = new MicroSuiteMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    14,
                    3,
                    37.5,
                    200.0,
                    0
            );

            RentMgd testRent3 = new RentMgd(
                    new UniqueIdMgd(UUID.randomUUID()),
                    "C4",
                    LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                    testGuest2,
                    testRoom2
            );

            try {
                testRentMongoDecoratedRepository.addRemote(testRent3);
            } catch (Exception e) {
                if(e.getMessage() ==
                        "Cannot rent room, there are no free rooms."){
                    exeptionChecked = true;
                }
            }

            assertTrue(exeptionChecked);

            // CLEAN UP
            testRentMongoDecoratedRepository.removeRemote(testRent
                    .getEntityId());
            foundRent =
                    (RentMgd) testRentMongoDecoratedRepository.findRemote(testRent
                            .getEntityId());
            assertEquals(foundRent,
                    null);

            RedisDecoratedRepository redisDecoratedRepository =
                    (RedisDecoratedRepository)(testRentMongoDecoratedRepository
                            .getiMongoRepositorywrapper());
            RentRepository mongoRepository =
                    (RentRepository)(redisDecoratedRepository
                            .getiMongoRepositorywrapper());
            mongoRepository.dropCollection();

            GuestRepository guestRepository = new GuestRepository(connectionManager);
            RoomRepository roomRepository = new RoomRepository(connectionManager);

            guestRepository.dropCollection();
            roomRepository.dropCollection();
        }
    }
}
