package com.cantabpi.countdown;

public enum Operation {
    PLUS("+"),
    TIMES("*");


    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public int apply(int a, int b) {
        return switch (this) {
            case PLUS -> a + b;
            case TIMES -> a * b;
        };
    }
}
