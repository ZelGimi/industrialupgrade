package com.denfop.api.tile;


import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@MethodsReturnNonnullByDefault
public interface IMultiTileBlock extends ISubEnum {

    Material MACHINE = new Material(MapColor.IRON) {
        {
            setRequiresTool();
            setImmovableMobility();
        }

    };
    Material CABLE = new Material(MapColor.IRON) {
        {
            setImmovableMobility();
        }

    };

    ResourceLocation getIdentifier();

    boolean hasItem();

    default boolean hasOtherVersion() {
        return false;
    }

    default List<ItemStack> getOtherVersion(ItemStack stack) {
        return Collections.singletonList(stack);
    }

    @Nullable
    Class<? extends TileEntityBlock> getTeClass();

    boolean hasActive();

    Set<EnumFacing> getSupportedFacings();

    float getHardness();

    MultiTileBlock.HarvestTool getHarvestTool();

    MultiTileBlock.DefaultDrop getDefaultDrop();

    boolean allowWrenchRotating();

    void buildDummies();

    @Nullable
    TileEntityBlock getDummyTe();

    default CreativeTabs getCreativeTab() {
        return IUCore.IUTab;
    }

    default String[] getMultiModels() {
        return new String[0];
    }

    default Material getMaterial() {
        return MACHINE;
    }

}
