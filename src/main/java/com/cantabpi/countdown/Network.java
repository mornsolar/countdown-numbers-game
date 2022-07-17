package com.cantabpi.countdown;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class Network {

    private final Map<Integer, SortedSet<Triangle>> trianglesByA = new TreeMap<>();
    private final Map<Integer, SortedSet<Triangle>> trianglesByB = new TreeMap<>();
    private final Map<Integer, SortedSet<Triangle>> trianglesByC = new TreeMap<>();

    public void addTriangle(Triangle triangle) {
        trianglesByA.computeIfAbsent(triangle.getA().value(), k -> new ConcurrentSkipListSet<>(Triangle.A_COMPARATOR)).add(triangle);
        trianglesByB.computeIfAbsent(triangle.getB().value(), k -> new ConcurrentSkipListSet<>(Triangle.B_COMPARATOR)).add(triangle);
        trianglesByC.computeIfAbsent(triangle.getC().value(), k -> new ConcurrentSkipListSet<>(Triangle.C2_COMPARATOR)).add(triangle);
    }

    public Set<Triangle> getTrianglesWithA(int a) {
        SortedSet<Triangle> triangles = trianglesByA.get(a);
        return Objects.requireNonNullElse(triangles, Collections.emptySet());
    }

    public Set<Triangle> getTrianglesWithB(int b) {
        SortedSet<Triangle> triangles = trianglesByB.get(b);
        return Objects.requireNonNullElse(triangles, Collections.emptySet());
    }
    public Set<Triangle> getTrianglesWithC(int c) {
        SortedSet<Triangle> triangles = trianglesByC.get(c);
        return Objects.requireNonNullElse(triangles, Collections.emptySet());
    }
}
