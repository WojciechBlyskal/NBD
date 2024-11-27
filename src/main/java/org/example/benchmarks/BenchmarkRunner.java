package org.example.benchmarks;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

public class BenchmarkRunner {

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(Benchmarks
                        .class.getSimpleName())
                .mode(Mode.SingleShotTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupForks(1)
                .forks(1)
                .warmupIterations(3)
                .warmupTime(TimeValue.seconds(60))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(60))
                .build();

        new Runner(opt).run();
    }
}
