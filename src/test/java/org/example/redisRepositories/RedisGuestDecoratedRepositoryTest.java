package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.mongoRepositories.ConnectionManager;
//import org.example.mongoRepositories.MongoDecoratedRepository;
import org.example.Mgd.GuestMgd;
import org.example.mongoRepositories.GuestRepository;
import org.example.mongoRepositories.MongoDecoratedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.redisRepositories.Decorators.GuestDecorator;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedisGuestDecoratedRepositoryTest {

    GuestMgd testGuest;

    @BeforeEach
    public void setUp(){

        testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                3000,
                "Adam",
                "Kowalski",
                "123456789"
        );

    }

    @Test
    public void remoteRedisTest() throws JsonProcessingException {
        try (RedisRepository redisRepository
                     = new RedisRepository("redisConnectionFiles/workingCluster")){

            RedisDecoratedRepository testGuestRedisDecoratedRepository =
                    new RedisDecoratedRepository(
                            new GuestDecorator(
                                    redisRepository
                            )
                    );

            testGuestRedisDecoratedRepository.addToCache(testGuest);

            GuestMgd foundGuest = (GuestMgd) testGuestRedisDecoratedRepository.findInCache(testGuest
                    .getEntityId());
            assertEquals(foundGuest.getEntityId().getUuid(),
                    testGuest.getEntityId().getUuid());

            testGuestRedisDecoratedRepository.deleteFromCache(testGuest
                    .getEntityId());

            boolean notFound = false;
            try {
                foundGuest = (GuestMgd) testGuestRedisDecoratedRepository.findInCache(testGuest
                        .getEntityId());
            } catch (NoSuchElementException noSuchElementException){
                notFound = true;
            }
            assertEquals(notFound,
                    true);
        }
    }

    @Test
    public void notConnectedTest() throws Exception {

        try (RedisRepository redisRepository
                     = new RedisRepository("redisConnectionFiles/notWorkingCluster");
             ConnectionManager connectionManager
                     = new ConnectionManager()) {

            MongoDecoratedRepository testGuestMongoDecoratedRepository =
                    new MongoDecoratedRepository(
                            new RedisDecoratedRepository(
                                    new GuestDecorator(
                                            redisRepository
                                    ),
                                    new GuestRepository(connectionManager)
                            )
                    );

            testGuestMongoDecoratedRepository.addRemote(testGuest);

            GuestMgd foundGuest =
                    (GuestMgd) testGuestMongoDecoratedRepository.findRemote(testGuest
                            .getEntityId());
            assertEquals(foundGuest.getEntityId().getUuid(),
                    testGuest.getEntityId().getUuid());

            testGuestMongoDecoratedRepository.removeRemote(testGuest.getEntityId());
            foundGuest =
                    (GuestMgd) testGuestMongoDecoratedRepository.findRemote(testGuest
                            .getEntityId());
            assertEquals(foundGuest,
                    null);

            RedisDecoratedRepository redisDecoratedRepository =
                    (RedisDecoratedRepository)(testGuestMongoDecoratedRepository
                            .getiMongoRepositorywrapper());
            GuestRepository mongoRepository =
                    (GuestRepository)(redisDecoratedRepository
                            .getiMongoRepositorywrapper());
            mongoRepository.dropCollection();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
