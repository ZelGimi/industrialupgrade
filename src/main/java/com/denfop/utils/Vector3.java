package com.denfop.utils;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Vector3 {

    public static Vector3 one = new Vector3(1.0D, 1.0D, 1.0D);
    public static Vector3 down = new Vector3(0.0D, -1.0D, 0.0D);
    public static Vector3 center = new Vector3(0.5D, 0.5D, 0.5D);
    public double x;
    public double y;
    public double z;

    public Vector3() {
    }

    public Vector3(double d, double d1, double d2) {
        this.x = d;
        this.y = d1;
        this.z = d2;
    }


    public static Vector3 fromEntity(Entity e) {
        return new Vector3(e.posX, e.posY, e.posZ);
    }


    public Vec3d vec3() {
        return new Vec3d(this.x, this.y, this.z);
    }


    public Vector3 set(double x1, double y1, double z1) {
        this.x = x1;
        this.y = y1;
        this.z = z1;
        return this;
    }


    public Vector3 add(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        return this;
    }


}
