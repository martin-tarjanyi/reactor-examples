package com.example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ReactorExamples
{
    @Test
    public void just()
    {
        Mono.just("first-mono")
            .subscribe(s -> System.out.println(s));
    }

    @Test
    public void monoOperator()
    {
        Mono.just("first-mono")
            .map(String::toUpperCase)
            .subscribe(s -> System.out.println(s));
    }

    @Test
    public void monoError()
    {
        Mono.error(new IllegalArgumentException("this is an error"))
            .subscribe(s -> System.out.println(s), e -> System.out.println(e.getMessage()));
    }

    @Test
    public void firstFlux()
    {
        Flux.fromIterable(Arrays.asList("One", "Two", "Three", "Four"))
            .subscribe(System.out::println);
    }

    @Test
    public void zipFlux()
    {
        Flux<Integer> numberIntegersFlux = Flux.range(1, 4);
        Flux<String> numberStringsFlux = Flux.fromIterable(Arrays.asList("One", "Two", "Three", "Four"));

        Flux.zip(numberIntegersFlux, numberStringsFlux, (numberInteger, numberString) -> numberInteger + "-" + numberString)
            .subscribe(System.out::println);
    }

    @Test
    public void mergeDelayedFlux()
    {
        Flux<String> numberStringsFlux = Flux.fromIterable(Arrays.asList("One", "Two", "Three", "Four"))
                                             .delayElements(Duration.ofMillis(500));

        Flux<Integer> numberIntegersFlux = Flux.range(1, 4)
                                               .delayElements(Duration.ofMillis(1000));

        Flux.merge(numberStringsFlux, numberIntegersFlux)
            .doOnNext(System.out::println)
            .blockLast();
    }

    @Test
    public void repeatFlux()
    {
        Flux.fromIterable(Arrays.asList("One", "Two", "Three", "Four"))
            .repeat(4)
            .subscribe(System.out::println);
    }

    @Test
    public void retryMono()
    {
        Mono.fromCallable(() -> randomGenerator(1, 60))
            .doOnError(e -> System.out.println("Error happened."))
            .retry(5)
            .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private int randomGenerator(int lowerBound, int upperBound)
    {
        int randomNumber = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);

        if (randomNumber < 50)
        {
            throw new IllegalArgumentException("Too low number");
        }

        return randomNumber;
    }
}
