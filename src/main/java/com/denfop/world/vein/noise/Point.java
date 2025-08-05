package com.denfop.world.vein.noise;

import java.util.Objects;

public class Point {
    public short x;
    public short y;
    public Point(int x, int y) { this.x = (short) x; this.y = (short) y; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
