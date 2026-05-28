package com.denfop.api.pollution.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public final class PollutionWeatherUtil {

    private PollutionWeatherUtil() {
    }

    public static float getPollutedPrecipitationStrength(Minecraft minecraft, PollutionVisualData state, float partialTick) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null || !state.isActive()) {
            return 0.0F;
        }

        float rainLevel = minecraft.level.getRainLevel(partialTick);
        if (rainLevel <= 0.001F) {
            return 0.0F;
        }

        float thunder = minecraft.level.getThunderLevel(partialTick);

        float pollutionWeight = Mth.clamp(
                Math.max(
                        state.getAirInfluence(),
                        Math.max(state.getSoilInfluence() * 0.60F, state.getRadiationInfluence() * 0.48F)
                ) + state.getOverallInfluence() * 0.24F,
                0.0F,
                1.0F
        );

        float weatherBoost = 1.0F + thunder * 0.18F;
        return Mth.clamp(rainLevel * pollutionWeight * weatherBoost, 0.0F, 1.0F);
    }

    public static boolean isSnowAtPlayer(Minecraft minecraft) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            return false;
        }

        LocalPlayer player = minecraft.player;
        return minecraft.level.getBiome(player.blockPosition()).value().coldEnoughToSnow(player.blockPosition());
    }

    private static int mixPair(int first, int second, float t) {
        return PollutionVisualColors.lerpColor(first, second, t);
    }

    public static int getPrecipitationColor(PollutionVisualData state, boolean snow) {
        float air = state.getAirInfluence();
        float soil = state.getSoilInfluence();
        float radiation = state.getRadiationInfluence();

        if (snow) {
            int color = 0xD0D0D0;

            if (air > 0.01F) {
                int airSnow = mixPair(0xD0CCC4, PollutionVisualColors.AIR_OVERLAY, 0.72F);
                color = PollutionVisualColors.lerpColor(color, airSnow, air * 0.95F);
            }

            if (soil > 0.01F) {
                int soilSnow = mixPair(0xC8C2B2, PollutionVisualColors.SOIL_OVERLAY, 0.68F);
                color = PollutionVisualColors.lerpColor(color, soilSnow, soil * 0.70F);
            }

            if (radiation > 0.01F) {
                int radSnow = mixPair(0xD8FFD2, PollutionVisualColors.RADIATION_OVERLAY, 0.78F);
                color = PollutionVisualColors.lerpColor(color, radSnow, radiation * 0.95F);
            }

            return color;
        }

        int color = PollutionVisualColors.lerpColor(state.getFogColor(), state.getWaterTintColor(), 0.28F);

        if (air > 0.01F) {
            int airRain = mixPair(PollutionVisualColors.AIR_FOG, PollutionVisualColors.AIR_OVERLAY, 0.58F);
            color = PollutionVisualColors.lerpColor(color, airRain, air * 0.95F);
        }

        if (soil > 0.01F) {
            int soilRain = mixPair(PollutionVisualColors.SOIL_FOG, PollutionVisualColors.SOIL_OVERLAY, 0.52F);
            color = PollutionVisualColors.lerpColor(color, soilRain, soil * 0.65F);
        }

        if (radiation > 0.01F) {
            int radRain = mixPair(PollutionVisualColors.RADIATION_FOG, PollutionVisualColors.RADIATION_OVERLAY, 0.60F);
            color = PollutionVisualColors.lerpColor(color, radRain, radiation * 0.92F);
        }

        return color;
    }


    public static boolean shouldReplaceVanillaWeather(Minecraft minecraft, PollutionVisualData state, float partialTick) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null || !state.isActive()) {
            return false;
        }

        if (minecraft.level.getRainLevel(partialTick) <= 0.001F) {
            return false;
        }

        return getPollutedPrecipitationStrength(minecraft, state, partialTick) > 0.025F;
    }


}