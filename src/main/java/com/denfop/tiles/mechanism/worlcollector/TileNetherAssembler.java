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

public class TileNetherAssembler extends TileBaseWorldCollector {

    public TileNetherAssembler() {
        super(EnumTypeCollector.NETHER);
    }

    public void init() {

        addRecipe(new ItemStack(Blocks.SAND), 0.75, new ItemStack(Blocks.SOUL_SAND));
        addRecipe(new ItemStack(Items.IRON_INGOT), 16, new ItemStack(Items.QUARTZ));
        addRecipe(new ItemStack(Items.BONE), 16, new ItemStack(Items.BLAZE_ROD));
        addRecipe(new ItemStack(Items.WHEAT_SEEDS), 16, new ItemStack(Items.NETHER_WART));
        addRecipe(new ItemStack(Blocks.COBBLESTONE), 0.75, new ItemStack(Blocks.NETHERRACK));
        addRecipe(new ItemStack(Items.GOLD_INGOT), 16, new ItemStack(Blocks.GLOWSTONE));
        addRecipe(new ItemStack(Items.SKULL), 32, new ItemStack(Items.SKULL, 1, 1));
        addRecipe(new ItemStack(Items.LAVA_BUCKET), 4, new ItemStack(Blocks.MAGMA));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.nether_assembler;
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
