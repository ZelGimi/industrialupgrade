package com.denfop.mixin;


import com.denfop.api.pollution.client.PollutionWeatherReplacementHook;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRendererWeather {

    @Inject(
            method = "renderSnowAndRain",
            at = @At("HEAD"),
            cancellable = true
    )
    private void iu$suppressVanillaWeather(
            LightTexture lightTexture,
            float partialTick,
            double cameraX,
            double cameraY,
            double cameraZ,
            CallbackInfo ci
    ) {
        if (PollutionWeatherReplacementHook.shouldSuppressVanillaWeather(partialTick)) {
            ci.cancel();
        }
    }
}