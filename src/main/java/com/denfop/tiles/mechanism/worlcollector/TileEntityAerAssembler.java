package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.TileEntityBaseWorldCollector;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAerAssembler extends TileEntityBaseWorldCollector {

    public TileEntityAerAssembler() {
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

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

}
