package com.denfop.render.crop;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class CropTextures {

    private final ResourceLocation location;
    private TextureAtlasSprite sprite;

    public CropTextures(ResourceLocation location){
        this.location=location;
        this.sprite = null;
    }

    public void setSprite(final TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public TextureAtlasSprite getSprite() {
        return sprite;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CropTextures that = (CropTextures) o;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

}
