package com.denfop.tiles.mechanism.radiation_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySimpleRadiationStorage extends TileEntityRadiationStorage {

    public TileEntitySimpleRadiationStorage() {
        super(4000000, EnumTypeStyle.DEFAULT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.radiation_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
