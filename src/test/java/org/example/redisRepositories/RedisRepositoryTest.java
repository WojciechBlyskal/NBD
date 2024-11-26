package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.GuestMgd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.redisRepositories.Decorators.GuestDecorator;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedisRepositoryTest {

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
    public void wrongFileTest() throws JsonProcessingException {

        try (RedisRepository redisRepository
                     = new RedisRepository("redisConnectionFiles/notExistingCluster")) {

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
        }
    }
}
