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

public class TileAquaAssembler extends TileBaseWorldCollector {

    public TileAquaAssembler() {
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.aqua_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
