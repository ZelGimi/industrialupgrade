package com.denfop.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public final class Vector3 {

    public static final Vector3 UP = new Vector3(0.0, 1.0, 0.0);
    public double x;
    public double y;
    public double z;

    public Vector3() {
    }

    public Vector3(double x1, double y1, double z1) {
        this.x = x1;
        this.y = y1;
        this.z = z1;
    }

    public Vector3(Vector3 v) {
        this(v.x, v.y, v.z);
    }

    public Vector3(Vec3d v) {
        this(v.x, v.y, v.z);
    }

    public static Vector3 fromEntityCenter(Entity e) {
        return new Vector3(e.posX, e.posY - e.getYOffset() + e.height / 2, e.posZ);
    }

    public static Vector3 fromEntity(Entity e) {
        return new Vector3(e.posX, e.posY, e.posZ);
    }

    public Vector3 subtract(double dx, double dy, double dz) {
        x -= dx;
        y -= dy;
        z -= dz;
        return this;
    }

    public Vector3 subtract(double d) {
        return subtract(d, d, d);
    }

    public Vector3 subtract(Vector3 vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public Vector3 copy(Vector3 dst) {
        return dst.set(this);
    }

    public Vector3 set(double vx, double vy, double vz) {
        this.x = vx;
        this.y = vy;
        this.z = vz;
        return this;
    }

    public Vector3 set(Vector3 v) {
        return this.set(v.x, v.y, v.z);
    }

    public Vector3 set(Vec3d v) {
        return this.set(v.x, v.y, v.z);
    }

    public Vector3 add(double vx, double vy, double vz) {
        this.x += vx;
        this.y += vy;
        this.z += vz;
        return this;
    }

    public Vector3 add(Vector3 v) {
        return this.add(v.x, v.y, v.z);
    }

    public Vector3 addScaled(Vector3 v, double scale) {
        return this.add(v.x * scale, v.y * scale, v.z * scale);
    }

    public Vector3 sub(double vx, double vy, double vz) {
        this.x -= vx;
        this.y -= vy;
        this.z -= vz;
        return this;
    }

    public Vector3 sub(Vector3 v) {
        return this.sub(v.x, v.y, v.z);
    }

    public Vector3 cross(double vx, double vy, double vz) {
        return this.set(this.y * vz - this.z * vy, this.z * vx - this.x * vz, this.x * vy - this.y * vx);
    }

    public Vector3 cross(Vector3 v) {
        return this.cross(v.x, v.y, v.z);
    }

    public double dot(double vx, double vy, double vz) {
        return this.x * vx + this.y * vy + this.z * vz;
    }

    public double dot(Vector3 v) {
        return this.dot(v.x, v.y, v.z);
    }

    public Vector3 normalize() {
        double len = this.length();
        this.x /= len;
        this.y /= len;
        this.z /= len;
        return this;
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public Vector3 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public double distanceSquared(double vx, double vy, double vz) {
        double dx = vx - this.x;
        double dy = vy - this.y;
        double dz = vz - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distanceSquared(Vector3 v) {
        return this.distanceSquared(v.x, v.y, v.z);
    }

    public double distanceSquared(Vec3d v) {
        return this.distanceSquared(v.x, v.y, v.z);
    }

    public double distance(double vx, double vy, double vz) {
        return Math.sqrt(this.distanceSquared(vx, vy, vz));
    }

    public double distance(Vector3 v) {
        return this.distance(v.x, v.y, v.z);
    }

    public double distance(Vec3d v) {
        return this.distance(v.x, v.y, v.z);
    }

    public Vector3 scale(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    public Vector3 scaleTo(double len) {
        double factor = len / this.length();
        return this.scale(factor);
    }

    public Vec3d toVec3() {
        return new Vec3d(this.x, this.y, this.z);
    }

    public String toString() {
        return "[ " + this.x + ", " + this.y + ", " + this.z + " ]";
    }

    public Vec3d vec3() {
        return new Vec3d(this.x, this.y, this.z);
    }

}
