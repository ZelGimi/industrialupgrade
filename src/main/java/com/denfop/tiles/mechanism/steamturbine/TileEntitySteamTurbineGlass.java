package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySteamTurbineGlass  extends TileEntityMultiBlockElement implements IGlass{

    public TileEntitySteamTurbineGlass(){}
    @Override
    public int getLevel() {
        return -1;
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_casing_glass;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }


    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return !(world.getTileEntity(otherPos) instanceof IGlass);
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }
}
