package com.cantabpi.countdown;

public enum LargeNumber implements CountdownNumber{
    TWENTY_FIVE(25, new int[]{0,0,2}, 1),
    FIFTY(50, new int[]{1,0,2}, 4),
    SEVENTY_FIVE(75, new int[]{0,1,2}, 4),
    HUNDRED(100, new int[]{2,0,2}, 7);
    private final int value;
    private final int nonTrivialFactors;
    private final int[] powerOfPrimes;

    LargeNumber(int value, int[] powerOfPrimes, int nonTrivialFactors) {
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
