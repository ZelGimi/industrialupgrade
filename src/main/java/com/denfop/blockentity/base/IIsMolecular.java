package com.denfop.blockentity.base;


import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IIsMolecular {

    int getMode();

    ItemStack getItemStack();

    BlockEntityBase getEntityBlock();

    @OnlyIn(Dist.CLIENT)
    BakedModel getBakedModel();

    @OnlyIn(Dist.CLIENT)
    default BakedModel getTransformedModel() {
        return null;
    }

}
