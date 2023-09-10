package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.TileBaseWorldCollector;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEnderAssembler extends TileBaseWorldCollector {

    public TileEnderAssembler() {
        super(EnumTypeCollector.END);
    }

    public void init() {

        addRecipe(new ItemStack(Items.MAGMA_CREAM), 32, new ItemStack(Items.ENDER_PEARL));
        addRecipe(new ItemStack(Items.APPLE), 64, new ItemStack(Items.CHORUS_FRUIT));
        addRecipe(new ItemStack(Items.SKULL), 64, new ItemStack(Items.SKULL, 1, 5));
        addRecipe(new ItemStack(Items.GLASS_BOTTLE), 64, new ItemStack(Items.EXPERIENCE_BOTTLE));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.ender_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

}
