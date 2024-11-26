package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.RentMgd;
import org.example.Mgd.RoomMgd;
import org.example.Mgd.StudioMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.mongoRepositories.ConnectionManager;
import org.example.mongoRepositories.MongoDecoratedRepository;
import org.example.mongoRepositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.redisRepositories.Decorators.RoomDecorator;

import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedisRoomDecoratedRepositoryTest {

    RoomMgd testRoom;

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
    public void remoteRedisTest() throws JsonProcessingException {
        try (RedisRepository redisRepository
                     = new RedisRepository("redisConnectionFiles/workingCluster")){

            RedisDecoratedRepository testRoomRedisDecoratedRepository =
                    new RedisDecoratedRepository(
                            new RoomDecorator(
                                    redisRepository
                            )
                    );

            testRoomRedisDecoratedRepository.addToCache(testRoom);

            RoomMgd foundRoom = (RoomMgd) testRoomRedisDecoratedRepository.findInCache(testRoom
                    .getEntityId());
            assertEquals(foundRoom.getEntityId().getUuid(),
                    testRoom.getEntityId().getUuid());

            testRoomRedisDecoratedRepository.deleteFromCache(testRoom
                    .getEntityId());

            boolean notFound = false;
            try {
                foundRoom = (RoomMgd) testRoomRedisDecoratedRepository.findInCache(testRoom
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

            MongoDecoratedRepository testRoomMongoDecoratedRepository =
                    new MongoDecoratedRepository(
                            new RedisDecoratedRepository(
                                    new RoomDecorator(
                                            redisRepository
                                    ),
                                    new RoomRepository(connectionManager)
                            )
                    );

            testRoomMongoDecoratedRepository
                    .addRemote(testRoom);

            RoomMgd foundRoom =
                    (RoomMgd) testRoomMongoDecoratedRepository.findRemote(testRoom
                            .getEntityId());
            assertEquals(foundRoom.getEntityId().getUuid(),
                    testRoom.getEntityId().getUuid());

            testRoomMongoDecoratedRepository.removeRemote(testRoom
                    .getEntityId());
            foundRoom =
                    (RoomMgd) testRoomMongoDecoratedRepository.findRemote(testRoom
                            .getEntityId());
            assertEquals(foundRoom,
                    null);

            RedisDecoratedRepository redisDecoratedRepository =
                    (RedisDecoratedRepository)(testRoomMongoDecoratedRepository
                            .getiMongoRepositorywrapper());
            RoomRepository mongoRepository =
                    (RoomRepository)(redisDecoratedRepository
                            .getiMongoRepositorywrapper());
            mongoRepository.dropCollection();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
