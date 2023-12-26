package com.denfop.blocks;

import com.denfop.Constants;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class StateMapperIU extends StateMapperBase {

    private final ResourceLocation identifier;

    public  StateMapperIU(final ResourceLocation identifier){
        this.identifier = identifier;
    }
    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return new ModelResourceLocation(this.identifier, getPropertyString(state.getProperties()));
    }




}
