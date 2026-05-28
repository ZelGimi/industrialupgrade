package com.denfop.items.armour.special;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

final class JetpackAnimationContext {

    final Player player;
    final boolean thrustActive;
    final boolean jetpackEnabled;
    final boolean verticalMode;
    final boolean jumpPressed;
    final boolean descendPressed;
    final boolean forwardPressed;
    final float energyRatio;
    final float partialTicks;
    final double verticalSpeed;
    final double horizontalSpeed;

    JetpackAnimationContext(
            final Player player,
            final boolean thrustActive,
            final boolean jetpackEnabled,
            final boolean verticalMode,
            final boolean jumpPressed,
            final boolean descendPressed,
            final boolean forwardPressed,
            final float energyRatio,
            final float partialTicks
    ) {
        this.player = player;
        this.thrustActive = thrustActive;
        this.jetpackEnabled = jetpackEnabled;
        this.verticalMode = verticalMode;
        this.jumpPressed = jumpPressed;
        this.descendPressed = descendPressed;
        this.forwardPressed = forwardPressed;
        this.energyRatio = Mth.clamp(energyRatio, 0.0F, 1.0F);
        this.partialTicks = partialTicks;

        final double motionX = player.getDeltaMovement().x;
        final double motionZ = player.getDeltaMovement().z;

        this.verticalSpeed = player.getDeltaMovement().y;
        this.horizontalSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    boolean isBoosting() {
        return this.thrustActive && this.forwardPressed && this.horizontalSpeed > 0.14D;
    }

    boolean isAirborne() {
        return !this.player.onGround() || this.player.getAbilities().flying;
    }
}