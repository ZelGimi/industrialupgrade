package com.denfop.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

public class StateMapperIU extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return null;
    }

    protected ModelResourceLocation getModelResourceLocation(ResourceLocation loc, final IBlockState state) {
        return new ModelResourceLocation(loc, getPropertyString(state.getProperties()));
    }

}
