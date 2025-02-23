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

public class TileEntityWindTurbineCasing extends TileEntityMultiBlockElement implements ICasing {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_casing_1;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine;
    }

}
