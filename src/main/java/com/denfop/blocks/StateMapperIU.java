package com.denfop.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StateMapperIU extends StateMapperBase {
    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return null;
    }

    protected ModelResourceLocation getModelResourceLocation(ResourceLocation loc, final IBlockState state) {
        return new ModelResourceLocation(loc, getPropertyString(state.getProperties()));
    }
}
