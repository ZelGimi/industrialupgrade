package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.TileEntityBaseWorldCollector;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityEnderAssembler extends TileEntityBaseWorldCollector {

    public TileEntityEnderAssembler() {
        super(EnumTypeCollector.END);
    }

    public void init() {

        addRecipe(new ItemStack(Items.MAGMA_CREAM), 32, new ItemStack(Items.ENDER_PEARL));
        addRecipe(new ItemStack(Items.APPLE), 64, new ItemStack(Items.CHORUS_FRUIT));
        addRecipe(new ItemStack(Items.SKULL), 64, new ItemStack(Items.SKULL, 1, 5));
        addRecipe(new ItemStack(Items.GLASS_BOTTLE), 64, new ItemStack(Items.EXPERIENCE_BOTTLE));

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
