package com.denfop.api.tile;


import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.state.Builder;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public interface IMultiTileBlock extends ISubEnum {

    Material MACHINE = new Builder(MaterialColor.METAL).notPushable().build();
    Material CABLE = new Builder(MaterialColor.WOOL).nonSolid().notPushable().build();

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
    Class<? extends TileEntityBlock> getTeClass();

    BlockEntityType<? extends TileEntityBlock> getBlockType();

    boolean hasActive();

    Set<Direction> getSupportedFacings();

    float getHardness();

    HarvestTool getHarvestTool();

    DefaultDrop getDefaultDrop();

    boolean allowWrenchRotating();

    void buildDummies();

    @Nullable
    TileEntityBlock getDummyTe();

    default CreativeModeTab getCreativeTab() {
        return IUCore.IUTab;
    }

    default String[] getMultiModels(final IMultiTileBlock teBlock) {
        return new String[0];
    }

    default Material getMaterial() {
        return MACHINE;
    }

    int ordinal();

    int getIDBlock();

    void setIdBlock(int id);

    void setType(RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockEntityType);

    void setDefaultState(BlockState blockState);
}
