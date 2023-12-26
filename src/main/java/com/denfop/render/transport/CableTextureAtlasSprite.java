package com.denfop.render.transport;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class CableTextureAtlasSprite extends TextureAtlasSprite {

    public CableTextureAtlasSprite(ResourceLocation spriteResourceLocation) {
        super(spriteResourceLocation.toString());
    }
    public boolean hasCustomLoader(net.minecraft.client.resources.IResourceManager manager, net.minecraft.util.ResourceLocation location)
    {
        return false;
    }
}
