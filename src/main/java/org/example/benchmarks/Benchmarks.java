package org.example.benchmarks;

import org.example.Mgd.RoomMgd;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class Benchmarks {

    @Benchmark
    public void mongoBenchmark(MongoBenchmarkPreparator mongoBenchmarkPreparator,
                               Blackhole blackhole) throws Exception {

        RoomMgd roomMgd = (RoomMgd) mongoBenchmarkPreparator.getMongoDecoratedRepository()
                .findRemote(
                        mongoBenchmarkPreparator.getTestRoom().getEntityId());
        blackhole.consume(roomMgd);
    }

    @Benchmark
    public void redisBenchmark(RedisBenchmarkPreparator redisBenchmarkPreparator,
                               Blackhole blackhole) throws Exception {

        RoomMgd roomMgd = (RoomMgd) redisBenchmarkPreparator.getMongoDecoratedRepository()
                .findRemote(redisBenchmarkPreparator.getTestRoom().getEntityId());
        blackhole.consume(roomMgd);
    }
}
