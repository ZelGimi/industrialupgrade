package com.denfop.tiles.windturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWindTurbineCasing1 extends TileEntityMultiBlockElement implements ICasing1 {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_casing_2;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine;
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
