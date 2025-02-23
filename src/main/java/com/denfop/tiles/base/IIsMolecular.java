package com.denfop.tiles.base;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIsMolecular {

    int getMode();

    ItemStack getItemStack();

    TileEntityBlock getEntityBlock();

    @SideOnly(Side.CLIENT)
    IBakedModel getBakedModel();

    @SideOnly(Side.CLIENT)
    default IBakedModel getTransformedModel() {
        return null;
    }

}
