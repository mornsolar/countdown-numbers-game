package com.cantabpi.countdown;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class CountdownNumberFactory {

    private static final List<Integer> primeNumbers = new ArrayList<>() {
    };
    private static final Map<Integer, CountdownNumber> numberCache = new TreeMap<>();

    static {
        primeNumbers.add(2);
        primeNumbers.add(3);
        primeNumbers.add(5);
        primeNumbers.add(7);
        for (int i = 1; i < 3000; i++) {
            of(i);
        }
    }

    public static CountdownNumber of(int number) {
        if (number <= 0) {
            throw new RuntimeException();
        }
        CountdownNumber countdownNumber = numberCache.get(number);
        if (countdownNumber != null) {
            return countdownNumber;
        } else if (number <= 10) {
            SmallNumber smallNumber = SmallNumber.values()[number - 1];
            numberCache.put(number, smallNumber);
            return smallNumber;
        } else if (number <= 100 && number % 25 == 0) {
            LargeNumber largeNumber = LargeNumber.values()[number / 25 - 1];
            numberCache.put(number, largeNumber);
            return largeNumber;
        } else {
            int[] powers = primeFactorisation(number);
            int nonTrivialFactors = nonTrivialFactors(powers);
            PositiveNumber positiveNumber = new PositiveNumber(number, powers, nonTrivialFactors);
            numberCache.put(number, positiveNumber);
            return positiveNumber;
        }
    }

    private static int nonTrivialFactors(int[] powers) {
        int factors = 1;
        for (int power : powers) {
            factors *= (1 + power);
        }
        return Math.max(factors - 2, 0);
    }

    public static int nonTrivialFactors(int value) {
        if (value <= 1) {
            return 0;
        }
        int toTest = value;
        boolean isPrime = true;
        int counter = 1;
        for (int primeNumber : primeNumbers) {
            int power = 0;
            if (primeNumber > toTest) {
                break;
            }
            while (toTest > 1 && toTest % primeNumber == 0) {
                // found a prime factor
                toTest = toTest / primeNumber;
                if (toTest != 1) {
                    isPrime = false;
                }
                power++;
            }
            counter *= (power + 1);
        }
        if (isPrime) {
            primeNumbers.add(value);
        }
        return Math.max(counter - 2, 0);
    }

    public static int[] primeFactorisation(int value) {
        if (value <= 1) {
            return new int[]{};
        }
        int toTest = value;
        boolean isPrime = true;
        int index = 0;
        int[] ret = new int[primeNumbers.size() + 1];
        for (int primeNumber : primeNumbers) {
            int power = 0;
            if (primeNumber > toTest) {
                break;
            }
            while (toTest > 1 && toTest % primeNumber == 0) {
                // found a prime factor
                toTest = toTest / primeNumber;
                if (toTest != 1) {
                    isPrime = false;
                }
                power++;
            }
            ret[index++] = power;
        }
        if (isPrime) {
            primeNumbers.add(value);
            ret[index] = 1;
        }
        return ret;
    }


    public static void main(String[] args) {
//        System.out.println(primeNumbers);
//        System.out.println(primeNumbers.size());

        Network network = new Network();
        for (int i = 1; i <= 100; i++) {
            for (int j = i; j <= 100; j++) {
//                System.out.printf("i is %s, j is %s%n", i, j);
                network.addTriangle(new Triangle(of(i), of(j), Operation.PLUS));
                if (i > 1) {
                    network.addTriangle(new Triangle(of(i), of(j), Operation.TIMES));
                }
            }
        }
//        System.out.println(primeNumbers);
        int target = 126;
        CountdownNumber targetNumber = of(target);
        CountdownNumber[] raws = new CountdownNumber[]{
                SmallNumber.TWO,
                SmallNumber.FOUR,
                SmallNumber.FIVE,
                SmallNumber.SEVEN,
                SmallNumber.EIGHT,
                LargeNumber.TWENTY_FIVE};

        for (int i = 0; i < 10; i++) {
            List<Triangle> route = new ArrayList<>(6);
            Map<SmallNumber, AtomicInteger> smallNumberRawCounts = Map.of(
                    SmallNumber.TWO, new AtomicInteger(1),
                    SmallNumber.FOUR, new AtomicInteger(1),
                    SmallNumber.FIVE, new AtomicInteger(1),
                    SmallNumber.SEVEN, new AtomicInteger(1),
                    SmallNumber.EIGHT, new AtomicInteger(1));

            Map<LargeNumber, AtomicBoolean> largeNumbersRawSet = Map.of(LargeNumber.TWENTY_FIVE, new AtomicBoolean(true));

            boolean search = getSearch(network, targetNumber, smallNumberRawCounts, largeNumbersRawSet, route);
        }

        Map<SmallNumber, AtomicInteger> smallNumberRawCounts = Map.of(
                SmallNumber.TWO, new AtomicInteger(1),
                SmallNumber.FOUR, new AtomicInteger(1),
                SmallNumber.FIVE, new AtomicInteger(1),
                SmallNumber.SEVEN, new AtomicInteger(1),
                SmallNumber.EIGHT, new AtomicInteger(1));

        Map<LargeNumber, AtomicBoolean> largeNumbersRawSet = Map.of(LargeNumber.TWENTY_FIVE, new AtomicBoolean(true));


        List<Triangle> route = new ArrayList<>(6);
        long l = System.nanoTime();
        boolean search = getSearch(network, targetNumber, smallNumberRawCounts, largeNumbersRawSet, route);
        System.out.println("Used nano:" + (System.nanoTime() - l));
        System.out.println(search);

        System.out.println(route);
    }

    private static boolean getSearch(Network network, CountdownNumber targetNumber, Map<SmallNumber, AtomicInteger> smallNumberCounts, Map<LargeNumber, AtomicBoolean> largeNumbersSet, List<Triangle> route) {
        int originalRouteSize = route.size();
        if (originalRouteSize <= 4) {
            for (Triangle triangle : network.getTrianglesWithC(targetNumber.value())) {
                route.add(triangle);
//                System.out.println(route);

                boolean bIsRawNumber = rawNumberUsedNew(smallNumberCounts, largeNumbersSet, triangle.getB());
                boolean aIsRawNumber = rawNumberUsedNew(smallNumberCounts, largeNumbersSet, triangle.getA());

                boolean ret;
                boolean bCanBeSolved;
                if (!bIsRawNumber) {
                    if (route.size() < 5) {
                        //try further in B
                        bCanBeSolved = getSearch(network, triangle.getB(), smallNumberCounts, largeNumbersSet, route);
                        if (!bCanBeSolved) {
                            ret = false;
                        } else {
                            //Now bCanBeSolved, can A be solved?
                            int bTerminalIndex = route.size();
                            if (aIsRawNumber) {
                                ret = true;
                            } else if (route.size() < 5) { // try further in A
                                ret = getSearch(network, triangle.getA(), smallNumberCounts, largeNumbersSet, route);
                            } else {
                                ret = false;
                            }

                            if (!ret) { //Need to restore the final two raw number usages from bLastTriangle
                                for (int i = originalRouteSize + 1; i < bTerminalIndex; i++) {
                                    restoreBy1Counter(route.get(i).getA(), largeNumbersSet, smallNumberCounts, route);
                                    restoreBy1Counter(route.get(i).getB(), largeNumbersSet, smallNumberCounts, route);
                                }

                            }

                        }
                    } else { // no points going deeper for B as would have used more than 5 operations
                        ret = false;
                    }
                } else { // B is a rawNumber
                    if (aIsRawNumber) {
                        ret = true;
                    } else if (route.size() < 5) { // try further in A
                        ret = getSearch(network, triangle.getA(), smallNumberCounts, largeNumbersSet, route);
                    } else {
                        ret = false;
                    }

                }

                if (!ret) {
                    if (aIsRawNumber) { // since it is failure, put back the value in the raw counter
                        restoreBy1Counter(triangle.getA(), largeNumbersSet, smallNumberCounts, route);
                    }
                    if (bIsRawNumber) { // since it is failure, put back the value in the raw counter
                        restoreBy1Counter(triangle.getB(), largeNumbersSet, smallNumberCounts, route);
                    }
                    if (route.size() > originalRouteSize) {
                        route.subList(originalRouteSize, route.size()).clear();
                    }
                } else {
                    return ret;
                }

            }
            if (route.size() > originalRouteSize) {
                route.subList(originalRouteSize, route.size()).clear();
            }
            return false;
        } else {
            return false;
        }
    }

    private static void restoreBy1Counter(CountdownNumber rawNumber, Map<LargeNumber, AtomicBoolean> largeNumbersSet, Map<SmallNumber, AtomicInteger> smallNumberCounts, List<Triangle> routes) {
        switch (rawNumber) {
            case LargeNumber l -> {
                AtomicBoolean exists = largeNumbersSet.get(l);
                if (exists != null && exists.compareAndSet(false, true)) {
//                    System.out.println(routes);
//                    System.out.printf("Restoring %s by 1%n", l);
//                    System.out.printf("Usage now: %s%n", largeNumbersSet);
                }
            }
            case SmallNumber s -> {
                AtomicInteger exist = smallNumberCounts.get(s);
                if (exist != null) {
                    int before = exist.getAndIncrement();
//                    System.out.println(routes);
//                    System.out.printf("Restoring %s by 1%n", s);
//                    System.out.printf("Before: %s , After: %s%n", before, exist.get());
//                    System.out.printf("Usage now: %s%n", smallNumberCounts);
                }
            }
            default -> {
            }
        }
    }

    private static boolean rawNumberUsedNew
            (Map<SmallNumber, AtomicInteger> smallNumberCounts, Map<LargeNumber, AtomicBoolean> largeNumbersSet, CountdownNumber a) {
        boolean terminate = false;
        switch (a) {
            case LargeNumber l -> {
                if (largeNumbersSet.getOrDefault(l, new AtomicBoolean(false)).compareAndExchange(true, false)) {
//                    System.out.println("Large number is used:" + l);
                    terminate = true;
                }

            }
            case SmallNumber s -> {
                if (smallNumberCounts.containsKey(s) && smallNumberCounts.get(s).getAndUpdate(new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int operand) {
                        return operand > 0 ? operand - 1 : 0;
                    }
                }) > 0) {
//                    System.out.println("Small number is used:" + s);
                    terminate = true;
                }
            }
            default -> {//do nothing}
            }
        }
        return terminate;
    }

    private static boolean rawNumberUsed
            (Map<SmallNumber, Integer> smallNumberCounts, EnumSet<LargeNumber> largeNumbersSet, CountdownNumber a) {
        boolean terminate = false;
        switch (a) {
            case LargeNumber l -> {
                if (largeNumbersSet.remove(l)) {
//                    System.out.println("Large number is used:" + l);
                    terminate = true;
                }

            }
            case SmallNumber s -> {
                if (smallNumberCounts.computeIfPresent(s, (k, c) -> (c == 0) ? null : (c - 1)) != null) {
//                    System.out.println("Small number is used:" + s);
                    terminate = true;
                }
            }
            default -> {//do nothing}
            }
        }
        return terminate;
    }
}
