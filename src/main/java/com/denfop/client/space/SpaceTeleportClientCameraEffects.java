package com.denfop.client.space;

import com.denfop.Constants;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class SpaceTeleportClientCameraEffects {

    private SpaceTeleportClientCameraEffects() {
    }

    @SubscribeEvent
    public static void onComputeFov(final ViewportEvent.ComputeFov event) {
        SpaceTeleportClientState state = SpaceTeleportClientState.INSTANCE;
        if (!state.hasFx()) {
            return;
        }

        float p = state.getFxProgress();
        float boost = 0.0f;

        switch (state.getFxType()) {
            case OUTBOUND_CHARGE -> boost = 2.5f + 7.5f * easeInOutCubic(p);
            case RETURN_CHARGE -> boost = 1.5f + 5.5f * easeInOutCubic(p);
            case OUTBOUND_TUNNEL -> {
                float center = 1.0f - Math.abs(p - 0.5f) * 2.0f;
                boost = 9.0f + 14.0f * Mth.clamp(center, 0.0f, 1.0f);
            }
            case RETURN_TUNNEL -> {
                float center = 1.0f - Math.abs(p - 0.5f) * 2.0f;
                boost = 7.0f + 11.0f * Mth.clamp(center, 0.0f, 1.0f);
            }
            case ARRIVAL -> boost = 8.0f * (1.0f - easeOutCubic(p));
            case RETURN_ARRIVAL -> boost = 6.5f * (1.0f - easeOutCubic(p));
            default -> {
            }
        }

        event.setFOV(event.getFOV() + boost);
    }

    @SubscribeEvent
    public static void onComputeCameraAngles(final ViewportEvent.ComputeCameraAngles event) {
        SpaceTeleportClientState state = SpaceTeleportClientState.INSTANCE;
        if (!state.hasFx()) {
            return;
        }

        float p = state.getFxProgress();
        long time = System.currentTimeMillis();

        float yawOffset = 0.0f;
        float pitchOffset = 0.0f;
        float rollOffset = 0.0f;

        switch (state.getFxType()) {
            case OUTBOUND_CHARGE -> {
                float amp = 0.15f + 0.55f * easeInOutCubic(p);
                yawOffset += Mth.sin((float) (time * 0.018)) * amp;
                pitchOffset += Mth.cos((float) (time * 0.022)) * amp * 0.65f;
                rollOffset += Mth.sin((float) (time * 0.014)) * amp * 0.45f;
            }
            case RETURN_CHARGE -> {
                float amp = 0.20f + 0.45f * easeInOutCubic(p);
                yawOffset += Mth.sin((float) (time * 0.024)) * amp * 0.7f;
                pitchOffset += Mth.cos((float) (time * 0.019)) * amp;
                rollOffset += Mth.sin((float) (time * 0.021)) * amp * 0.8f;
            }
            case OUTBOUND_TUNNEL -> {
                float center = Mth.clamp(1.0f - Math.abs(p - 0.5f) * 2.0f, 0.0f, 1.0f);
                float amp = 0.5f + center * 2.0f;
                yawOffset += Mth.sin((float) (time * 0.038)) * amp * 0.5f;
                pitchOffset += Mth.cos((float) (time * 0.034)) * amp * 0.45f;
                rollOffset += Mth.sin((float) (time * 0.028)) * amp * 1.35f;
            }
            case RETURN_TUNNEL -> {
                float center = Mth.clamp(1.0f - Math.abs(p - 0.5f) * 2.0f, 0.0f, 1.0f);
                float amp = 0.45f + center * 1.75f;
                yawOffset += Mth.sin((float) (time * 0.042)) * amp * 0.4f;
                pitchOffset += Mth.cos((float) (time * 0.031)) * amp * 0.7f;
                rollOffset -= Mth.sin((float) (time * 0.026)) * amp * 1.6f;
            }
            case ARRIVAL -> {
                float amp = 1.8f * (1.0f - easeOutCubic(p));
                pitchOffset += Mth.sin((float) (time * 0.020)) * amp * 0.45f;
                rollOffset += Mth.cos((float) (time * 0.017)) * amp * 0.35f;
            }
            case RETURN_ARRIVAL -> {
                float amp = 1.5f * (1.0f - easeOutCubic(p));
                yawOffset += Mth.sin((float) (time * 0.016)) * amp * 0.35f;
                pitchOffset += Mth.cos((float) (time * 0.021)) * amp * 0.55f;
                rollOffset -= Mth.sin((float) (time * 0.018)) * amp * 0.4f;
            }
            default -> {
            }
        }

        event.setYaw(event.getYaw() + yawOffset);
        event.setPitch(event.getPitch() + pitchOffset);
        event.setRoll(event.getRoll() + rollOffset);
    }

    private static float easeInOutCubic(final float t) {
        if (t < 0.5f) {
            return 4f * t * t * t;
        }
        return 1f - (float) Math.pow(-2f * t + 2f, 3f) / 2f;
    }

    private static float easeOutCubic(final float t) {
        return 1f - (float) Math.pow(1f - t, 3f);
    }
}