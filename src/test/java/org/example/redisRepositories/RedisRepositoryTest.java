package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RoomMgd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedisRepositoryTest {

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
    public void wrongFileTest() throws JsonProcessingException {

        try (RedisRepository redisRepository
                     = new RedisRepository("docker/notExistingCluster")) {

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
        }
    }
}
