package com.denfop.blocks;

import com.denfop.blocks.state.ISkipProperty;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class StateMapperIU extends StateMapperBase {

    private final ResourceLocation identifier;

    public StateMapperIU(final ResourceLocation identifier) {
        this.identifier = identifier;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return new ModelResourceLocation(this.identifier, getPropertyString(state.getProperties()));
    }

    public <T extends Comparable<T>> String getPropertyName(IProperty<T> p_187489_1_, Comparable<?> p_187489_2_) {
        return p_187489_1_.getName((T) p_187489_2_);
    }

    public String getPropertyString(Map<IProperty<?>, Comparable<?>> p_178131_1_) {
        StringBuilder lvt_2_1_ = new StringBuilder();

        for (Map.Entry<IProperty<?>, Comparable<?>> iPropertyComparableEntry : p_178131_1_.entrySet()) {
            if (iPropertyComparableEntry.getKey() instanceof ISkipProperty) {
                continue;
            }
            if (lvt_2_1_.length() != 0) {
                lvt_2_1_.append(",");
            }

            IProperty<?> lvt_5_1_ = iPropertyComparableEntry.getKey();
            lvt_2_1_.append(lvt_5_1_.getName());
            lvt_2_1_.append("=");
            lvt_2_1_.append(this.getPropertyName(lvt_5_1_, iPropertyComparableEntry.getValue()));
        }

        if (lvt_2_1_.length() == 0) {
            lvt_2_1_.append("normal");
        }

        return lvt_2_1_.toString();
    }


}
