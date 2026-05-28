package com.denfop.screen.space;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class SpaceProjectionHelper {

    private SpaceProjectionHelper() {
    }

    public static CameraSpacePoint worldToCamera(Vec3 world, SpaceMapCamera camera) {
        double yawRad = Math.toRadians(camera.getYaw());
        double pitchRad = Math.toRadians(camera.getPitch());

        double cosY = Math.cos(yawRad);
        double sinY = Math.sin(yawRad);
        double cosP = Math.cos(pitchRad);
        double sinP = Math.sin(pitchRad);

        double x = world.x - camera.getPanX();
        double y = world.y - camera.getPanY();
        double z = world.z;

        double x1 = x * cosY - z * sinY;
        double z1 = x * sinY + z * cosY;

        double y2 = y * cosP - z1 * sinP;
        double z2 = y * sinP + z1 * cosP;

        double cameraZ = z2 + camera.getDistance();
        return new CameraSpacePoint((float) x1, (float) y2, (float) cameraZ);
    }

    public static ProjectedPoint project(Vec3 world,
                                         SpaceMapCamera camera,
                                         float centerX,
                                         float centerY,
                                         float focalLength) {
        CameraSpacePoint cam = worldToCamera(world, camera);
        if (cam.z <= 1.0f) {
            return new ProjectedPoint(0, 0, cam.z, false, 0);
        }

        float scale = focalLength / cam.z;
        float sx = centerX + cam.x * scale;
        float sy = centerY - cam.y * scale;
        return new ProjectedPoint(sx, sy, cam.z, true, scale);
    }

    public static float projectRadius(float worldRadius,
                                      Vec3 world,
                                      SpaceMapCamera camera,
                                      float focalLength) {
        CameraSpacePoint cam = worldToCamera(world, camera);
        if (cam.z <= 1.0f) {
            return 0.0f;
        }
        return Math.max(1.0f, worldRadius * (focalLength / cam.z));
    }

    public static float clampFocal(float viewportSize) {
        return Mth.clamp(viewportSize * 0.75f, 70.0f, 240.0f);
    }

    public record CameraSpacePoint(float x, float y, float z) {
    }

    public record ProjectedPoint(float x, float y, float depth, boolean visible, float scale) {
    }
}