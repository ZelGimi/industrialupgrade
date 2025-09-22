package com.denfop.tiles.lightning_rod;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockLightningRod;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityLightningRodAntennaMast extends TileEntityMultiBlockElement implements IAntennaMast {

    public TileEntityLightningRodAntennaMast() {
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockLightningRod.lightning_rod_antenna_mast;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.lightning_rod;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

}
