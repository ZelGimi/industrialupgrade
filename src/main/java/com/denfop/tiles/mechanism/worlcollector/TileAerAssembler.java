package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.TileBaseWorldCollector;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileAerAssembler extends TileBaseWorldCollector {

    public TileAerAssembler() {
        super(EnumTypeCollector.AER);
    }

    public void init() {
        addRecipe(new ItemStack(Blocks.SAPLING, 1, 0), getMatterFromEnergy(500000), new ItemStack(Blocks.SAPLING, 1, 1));
        addRecipe(new ItemStack(Blocks.SAPLING, 1, 1), getMatterFromEnergy(500000), new ItemStack(Blocks.SAPLING, 1, 2));
        addRecipe(new ItemStack(Blocks.SAPLING, 1, 2), getMatterFromEnergy(500000), new ItemStack(Blocks.SAPLING, 1, 3));
        addRecipe(new ItemStack(Blocks.SAPLING, 1, 3), getMatterFromEnergy(500000), new ItemStack(Blocks.SAPLING, 1, 4));
        addRecipe(new ItemStack(Blocks.SAPLING, 1, 4), getMatterFromEnergy(500000), new ItemStack(Blocks.SAPLING, 1, 5));
        addRecipe(new ItemStack(Blocks.SAPLING, 1, 5), getMatterFromEnergy(500000), new ItemStack(Blocks.SAPLING, 1, 0));
        addRecipe(new ItemStack(Items.WHEAT), getMatterFromEnergy(125000), new ItemStack(Blocks.HAY_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 4), getMatterFromEnergy(4000000), new ItemStack(Blocks.SPONGE));
        addRecipe(new ItemStack(Items.DYE, 1, 15), getMatterFromEnergy(4000000), new ItemStack(Items.FEATHER));
        addRecipe(new ItemStack(Items.WHEAT_SEEDS), getMatterFromEnergy(20000000), new ItemStack(Items.MELON_SEEDS));
        addRecipe(new ItemStack(Items.MELON_SEEDS), getMatterFromEnergy(2000000), new ItemStack(Items.PUMPKIN_SEEDS));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.aer_assembler;
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
