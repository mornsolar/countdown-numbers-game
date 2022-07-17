package com.cantabpi.countdown;

public record PositiveNumber(int value, int[] powerOfPrimes, int nonTrivialFactors) implements CountdownNumber {
    @Override
    public String toString() {
        return "PositiveNumber{" +
                "value=" + value +
                '}';
    }
}
