package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySolarGenerator extends TileSolarPanel {


    public TileEntitySolarGenerator(BlockPos pos, BlockState state) {
        super(EnumSolarPanels.SOLAR_PANEL_DEFAULT, BlockBaseMachine3.solar_iu, pos, state);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solar_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
