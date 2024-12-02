package org.example.benchmarks;

import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RoomMgd;
import org.example.mongoRepositories.ConnectionManager;
import org.example.mongoRepositories.MongoDecoratedRepository;
import org.example.mongoRepositories.RoomRepository;
import org.example.redisRepositories.RoomDecorator;
import org.openjdk.jmh.annotations.*;
import org.example.redisRepositories.RedisDecoratedRepository;
import org.example.redisRepositories.RedisRepository;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.UUID;

@State(Scope.Thread)
public class MongoBenchmarkPreparator {

    ConnectionManager connectionManager;
    RedisRepository redisRepository;

    MongoDecoratedRepository mongoDecoratedRepository;
    RedisDecoratedRepository redisDecoratedRepository;
    RoomRepository roomRepository;

    private MicroSuiteMgd testRoom;

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
                new RedisRepository("docker/redisCluster");
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
        roomRepository = (RoomRepository)(redisDecoratedRepository
                        .getiMongoRepositorywrapper());

        roomRepository.addRemote(testRoom);
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
