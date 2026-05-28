package com.denfop.screen.space;

import net.minecraft.util.Mth;

public class SpaceMapCamera {

    private float yaw = 35.0f;
    private float pitch = 22.0f;

    private float distance = 85.0f;
    private float targetDistance = 85.0f;

    private float panX = 0.0f;
    private float panY = 0.0f;

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getDistance() {
        return distance;
    }

    public float getPanX() {
        return panX;
    }

    public float getPanY() {
        return panY;
    }

    public void rotate(float deltaYaw, float deltaPitch) {
        this.yaw += deltaYaw;
        this.pitch = Mth.clamp(this.pitch + deltaPitch, -80.0f, 80.0f);
    }

    public void zoom(float delta) {
        this.targetDistance = Mth.clamp(this.targetDistance + delta, -1000.0f, 1000.0f);
    }

    public void pan(float dx, float dy) {
        float yawRad = (float) Math.toRadians(this.yaw);
        float cos = Mth.cos(yawRad);
        float sin = Mth.sin(yawRad);

        this.panX += (dx * cos - dy * sin) * 0.6f;
        this.panY += dy * 0.6f;


    }

    // Подтягиваем камеру к объекту по экрану + делаем почти close-up
    public void followProjected(float targetScreenX, float targetScreenY, float centerX, float centerY, float desiredDistance) {
        float dx = targetScreenX - centerX;
        float dy = targetScreenY - centerY;

        this.panX -= dx * 0.085f;
        this.panY -= dy * 0.085f;

        this.panX = Mth.clamp(this.panX, -220.0f, 220.0f);
        this.panY = Mth.clamp(this.panY, -220.0f, 220.0f);

        this.targetDistance = Mth.clamp(desiredDistance, 18.0f, 220.0f);
    }

    public void tick() {
        this.distance = Mth.lerp(0.16f, this.distance, this.targetDistance);
    }

    public void reset() {
        this.yaw = 35.0f;
        this.pitch = 22.0f;
        this.distance = 85.0f;
        this.targetDistance = 85.0f;
        this.panX = 0.0f;
        this.panY = 0.0f;
    }
}