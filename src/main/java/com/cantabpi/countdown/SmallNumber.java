package com.cantabpi.countdown;

public enum SmallNumber implements CountdownNumber {
    ONE(1, new int[]{}, 0),
    TWO(2, new int[]{1}, 0),
    THREE(3, new int[]{0, 1}, 0),
    FOUR(4, new int[]{2}, 1),
    FIVE(5, new int[]{0, 0, 1}, 0),
    SIX(6, new int[]{1, 1}, 2),
    SEVEN(7, new int[]{0, 0, 0, 1}, 0),
    EIGHT(8, new int[]{3}, 2),
    NINE(9, new int[]{0, 2}, 1),
    TEN(10, new int[]{1, 0, 1}, 2);
    private final int value;
    private final int[] powerOfPrimes;
    private final int nonTrivialFactors;

    SmallNumber(int value, int[] powerOfPrimes, int nonTrivialFactors) {
        this.value = value;
        this.powerOfPrimes = powerOfPrimes;
        this.nonTrivialFactors = nonTrivialFactors;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public int nonTrivialFactors() {
        return nonTrivialFactors;
    }

    @Override
    public int[] powerOfPrimes() {
        return powerOfPrimes;
    }
}
