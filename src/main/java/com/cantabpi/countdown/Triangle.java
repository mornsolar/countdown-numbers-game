package com.cantabpi.countdown;

import java.util.Comparator;
import java.util.Objects;

public class Triangle {

    public static final Comparator<Triangle> A_COMPARATOR = new BComparator();
    public static final Comparator<Triangle> B_COMPARATOR = new BComparator();
    public static final Comparator<Triangle> C_COMPARATOR = new CComparator();
    public static final Comparator<Triangle> C2_COMPARATOR = new C2Comparator();
    private final CountdownNumber a;
    private final CountdownNumber b;
    private final Operation o;
    private final CountdownNumber c;

    // Caller should make sure that the order is ascending from a <= b
    public Triangle(CountdownNumber a, CountdownNumber b, Operation o) {
        this.a = a;
        this.b = b;
        this.o = o;
        this.c = CountdownNumberFactory.of(o.apply(a.value(), b.value()));
    }

    public CountdownNumber getA() {
        return a;
    }

    public CountdownNumber getB() {
        return b;
    }

    public Operation getO() {
        return o;
    }

    public CountdownNumber getC() {
        return c;
    }


    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a.value() +
                ", b=" + b.value() +
                ", o=" + o +
                ", c=" + c.value() +
                '}';
    }


    @Override
    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (o1 == null || getClass() != o1.getClass()) return false;
        Triangle triangle = (Triangle) o1;
        return a.equals(triangle.a) && b.equals(triangle.b) && o == triangle.o;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, o);
    }

    //Priority given to triangle with more the combined factors of b and c.
    public static class AComparator implements Comparator<Triangle> {
        @Override
        public int compare(Triangle o1, Triangle o2) {
            return Integer.compare(
                    CountdownNumberFactory.nonTrivialFactors(o2.b.value()) + CountdownNumberFactory.nonTrivialFactors(o2.c.value()),
                    CountdownNumberFactory.nonTrivialFactors(o1.b.value()) + CountdownNumberFactory.nonTrivialFactors(o1.c.value())
            );
        }
    }

    //Priority given to triangle with more the combined factors of a and c.
    public static class BComparator implements Comparator<Triangle> {
        @Override
        public int compare(Triangle o1, Triangle o2) {
            return Integer.compare(
                    CountdownNumberFactory.nonTrivialFactors(o2.a.value()) + CountdownNumberFactory.nonTrivialFactors(o2.c.value()),
                    CountdownNumberFactory.nonTrivialFactors(o1.a.value()) + CountdownNumberFactory.nonTrivialFactors(o1.c.value())
            );
        }
    }

    // The more the combined factors of a and b are, the more front the triangle.
    public static class CComparator implements Comparator<Triangle> {
        @Override
        public int compare(Triangle o1, Triangle o2) {
            return Integer.compare(
                    CountdownNumberFactory.nonTrivialFactors(o2.b.value()) + CountdownNumberFactory.nonTrivialFactors(o2.a.value()),
                    CountdownNumberFactory.nonTrivialFactors(o1.b.value()) + CountdownNumberFactory.nonTrivialFactors(o1.a.value())
            );
        }
    }

    public static class C2Comparator implements Comparator<Triangle> {
        private final Comparator<Triangle> cComparator = C_COMPARATOR;

        @Override
        public int compare(Triangle o1, Triangle o2) {
            int compare = Integer.compare(Math.max(o1.a.value(), o1.b.value()), Math.max(o2.a.value(), o2.b.value()));
            return compare != 0 ? compare : cComparator.compare(o1, o2);
        }
    }

}
