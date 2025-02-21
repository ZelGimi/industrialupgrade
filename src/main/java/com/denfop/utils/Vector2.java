package com.denfop.utils;

import java.util.Objects;

public class Vector2 {

    private final int x;
    private final int z;

    public Vector2(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && z == vector2.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

}
