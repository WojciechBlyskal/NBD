
package org.example.benchmarks;

import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RoomMgd;
import org.example.mongoRepositories.*;
import org.example.redisRepositories.RoomDecorator;
import org.openjdk.jmh.annotations.*;
import org.example.redisRepositories.RedisDecoratedRepository;
import org.example.redisRepositories.RedisRepository;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.UUID;

@State(Scope.Thread)
public class RedisBenchmarkPreparator {

    private ConnectionManager connectionManager;
    private RedisRepository redisRepository;

    private MongoDecoratedRepository mongoDecoratedRepository;
    private RedisDecoratedRepository redisDecoratedRepository;
    private RoomRepository roomRepository;

    private RoomMgd testRoom;

    @Setup(Level.Invocation)
    public void setUp() throws Exception {

        connectionManager = new ConnectionManager();

        testRoom = new MicroSuiteMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                12,
                3,
                37.5,
                200.0,
                0
        );

        redisRepository =
                new RedisRepository("redisConnectionFiles/workingCluster");
        mongoDecoratedRepository =
                new MongoDecoratedRepository(
                        new RedisDecoratedRepository(
                                new RoomDecorator(
                                        redisRepository
                                )
                                ,new RoomRepository(connectionManager)
                        )
                );
        redisDecoratedRepository =
                (RedisDecoratedRepository)(mongoDecoratedRepository
                        .getiMongoRepositorywrapper());
        roomRepository =
                (RoomRepository)(redisDecoratedRepository
                        .getiMongoRepositorywrapper());

        redisRepository.addToCache(testRoom);
    }

    @TearDown(Level.Invocation)
    public void cleanUp(){
        roomRepository.dropCollection();
        connectionManager.close();

        redisRepository.close();
    }

    public RoomMgd getTestRoom() {
        return testRoom;
    }

    public MongoDecoratedRepository getMongoDecoratedRepository() {
        return mongoDecoratedRepository;
    }
}
