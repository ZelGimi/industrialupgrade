package com.denfop.api.blockentity;


import com.denfop.IUCore;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public interface MultiBlockEntity extends ISubEnum {

    MapColor CABLE = MapColor.WOOL;

    boolean hasItem();

    default boolean hasUniqueName() {
        return false;
    }


    default String getUniqueName() {
        return "";
    }

    default boolean hasOtherVersion() {
        return false;
    }

    default List<ItemStack> getOtherVersion(ItemStack stack) {
        return Collections.singletonList(stack);
    }

    @Nullable
    Class<? extends BlockEntityBase> getTeClass();

    BlockEntityType<? extends BlockEntityBase> getBlockType();

    boolean hasActive();

    Set<Direction> getSupportedFacings();

    float getHardness();

    HarvestTool getHarvestTool();

    DefaultDrop getDefaultDrop();

    boolean allowWrenchRotating();

    void buildDummies();

    @Nullable
    BlockEntityBase getDummyTe();

    default CreativeModeTab getCreativeTab() {
        return IUCore.IUTab;
    }

    default String[] getMultiModels(final MultiBlockEntity teBlock) {
        return new String[0];
    }

    default MapColor getMaterial() {
        return MapColor.METAL;
    }

    int ordinal();

    int getIDBlock();

    void setIdBlock(int id);

    void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType);

    void setDefaultState(BlockState blockState);
}
