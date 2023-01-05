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

public class TileEntityNetherAssembler extends TileEntityBaseWorldCollector {

    public TileEntityNetherAssembler() {
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
