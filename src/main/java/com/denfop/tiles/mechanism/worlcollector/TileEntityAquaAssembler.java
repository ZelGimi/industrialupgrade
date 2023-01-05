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

public class TileEntityAquaAssembler extends TileEntityBaseWorldCollector {

    public TileEntityAquaAssembler() {
        super(EnumTypeCollector.AQUA);
    }

    public void init() {

        addRecipe(new ItemStack(Items.DYE, 1, 10), 32, new ItemStack(Items.SLIME_BALL));
        addRecipe(new ItemStack(Items.DYE, 1, 4), 8, new ItemStack(Items.DYE));
        addRecipe(new ItemStack(Items.WATER_BUCKET), 8, new ItemStack(Blocks.ICE));
        addRecipe(new ItemStack(Items.FISH, 1), 8, new ItemStack(Items.FISH, 1, 1));
        addRecipe(new ItemStack(Items.FISH, 1, 1), 8, new ItemStack(Items.FISH, 1, 2));
        addRecipe(new ItemStack(Items.FISH, 1, 2), 8, new ItemStack(Items.FISH, 1, 3));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 11), 32, new ItemStack(Blocks.LAPIS_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 5), 128, new ItemStack(Blocks.EMERALD_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 3), 128, new ItemStack(Blocks.DIAMOND_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 14), 32, new ItemStack(Blocks.REDSTONE_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 4), 64, new ItemStack(Blocks.GOLD_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 8), 64, new ItemStack(Blocks.IRON_BLOCK));
        addRecipe(new ItemStack(Blocks.WOOL, 1, 15), 32, new ItemStack(Blocks.COAL_BLOCK));
        addRecipe(new ItemStack(Items.WHEAT_SEEDS), 2, new ItemStack(Items.REEDS));

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
