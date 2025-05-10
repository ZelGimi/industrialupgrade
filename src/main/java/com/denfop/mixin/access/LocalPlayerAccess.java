package com.denfop.mixin.access;


import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface LocalPlayerAccess {
    @Accessor
    double getXLast();

    @Accessor
    double getYLast1();

    @Accessor
    double getZLast();
}
