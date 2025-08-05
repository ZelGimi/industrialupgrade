package com.denfop.mixin.access;

import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin( RenderChunkRegion.class)
public interface RenderChunkRegionAccessor {
    @Accessor
    Level getLevel();


}
