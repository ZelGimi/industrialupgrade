package com.denfop.render.streak;

import com.denfop.Constants;
import net.minecraft.resources.ResourceLocation;

public final class StreakRenderHelper {

    public static final ResourceLocation TEXTURE = ResourceLocation.tryParse(Constants.TEXTURES_ITEMS + "effect.png");

    private static final int[] RAINBOW_RED = {
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 240, 222, 186, 150, 124, 96, 67,
            40, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            18, 34, 56, 78, 102, 121, 145, 176, 201, 218, 230, 255, 255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255
    };

    private static final int[] RAINBOW_GREEN = {
            0, 24, 36, 54, 72, 96, 120, 145, 172, 192, 216, 234, 255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 234, 214, 195,
            176, 153, 137, 112, 94, 86, 55, 31, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    private static final int[] RAINBOW_BLUE = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 18, 32, 45, 68, 78, 103, 118, 138, 151, 178, 205, 221, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 240, 228, 208, 186, 165, 149, 132, 115, 102, 97, 76, 53, 32, 15, 0
    };

    private StreakRenderHelper() {
    }

    public static float[] resolveColor(PlayerStreakInfo info, float time, float alpha) {
        int[] rgb = resolveRgb(info, time);
        return new float[]{
                rgb[0] / 255.0F,
                rgb[1] / 255.0F,
                rgb[2] / 255.0F,
                alpha
        };
    }

    public static int[] resolveRgb(PlayerStreakInfo info, float time) {
        if (info != null && info.isRainbow()) {
            int index = Math.floorMod((int) time, RAINBOW_RED.length);
            return new int[]{
                    RAINBOW_RED[index],
                    RAINBOW_GREEN[index],
                    RAINBOW_BLUE[index]
            };
        }

        if (info == null || info.getRgb() == null) {
            return new int[]{0, 255, 255};
        }

        return new int[]{
                info.getRgb().getRed() & 0xFF,
                info.getRgb().getGreen() & 0xFF,
                info.getRgb().getBlue() & 0xFF
        };
    }

    public static int resolvePackedRgb(PlayerStreakInfo info, float time) {
        int[] rgb = resolveRgb(info, time);
        return ((rgb[0] & 0xFF) << 16) | ((rgb[1] & 0xFF) << 8) | (rgb[2] & 0xFF);
    }
}