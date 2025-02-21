package com.denfop.render.crop;

import com.denfop.api.energy.EnergyTick;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;



public class CropList<T extends CropTextures> extends ArrayList<T > {

    public TextureAtlasSprite getTextures(ResourceLocation o) {

        for (T cropTextures : this) {
            if (cropTextures.getLocation().equals(o)) {
                return cropTextures.getSprite();
            }
        }
        return null;
    }

}
