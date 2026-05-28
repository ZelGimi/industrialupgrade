package com.denfop.api.pollution.client;

import net.minecraft.client.Minecraft;

public final class PollutionWeatherReplacementHook {

    private PollutionWeatherReplacementHook() {
    }

    public static boolean shouldSuppressVanillaWeather(float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        PollutionVisualData state = PollutionVisualController.getCurrent();
        return PollutionWeatherUtil.shouldReplaceVanillaWeather(minecraft, state, partialTick);
    }
}