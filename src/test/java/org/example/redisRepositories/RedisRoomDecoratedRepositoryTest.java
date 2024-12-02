package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Mgd.RoomMgd;
import org.example.Mgd.MicroSuiteMgd;
import org.example.mongoRepositories.ConnectionManager;
import org.example.mongoRepositories.MongoDecoratedRepository;
import org.example.mongoRepositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        try (RedisRepository redisRepository = new RedisRepository("docker/redisCluster")){

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
    public void updateRemoteTest() throws Exception {
        try (RedisRepository redisRepository = new RedisRepository("docker/redisCluster")) {
            ConnectionManager connectionManager = new ConnectionManager();
            RedisDecoratedRepository redisDecoratedRepository =
                    new RedisDecoratedRepository(
                            new RoomDecorator(redisRepository),
                            new RoomRepository(connectionManager));

            redisDecoratedRepository.addRemote(testRoom);

            RoomMgd foundRoomInCache = (RoomMgd) redisDecoratedRepository.findInCache(testRoom.getEntityId());
            assertEquals(foundRoomInCache.getEntityId().getUuid(), testRoom.getEntityId().getUuid());

            Bson filter = Filters.eq("_id", testRoom.getEntityId().getUuid().toString());
            Bson update = Updates.set("price", 300.0);
            redisDecoratedRepository.updateRemote(filter, update);

            boolean notFoundInCache = false;
            try {
                redisDecoratedRepository.findInCache(testRoom.getEntityId());
            } catch (NoSuchElementException e) {
                notFoundInCache = true;
            }
            assertTrue(notFoundInCache);
        }
    }

    @Test
    public void cleanCacheTest() throws JsonProcessingException {
        try (RedisRepository redisRepository
                     = new RedisRepository("docker/redisCluster")){

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

            testRoomRedisDecoratedRepository.cleanCache();

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
                     = new RedisRepository("docker/notWorkingCluster");
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
