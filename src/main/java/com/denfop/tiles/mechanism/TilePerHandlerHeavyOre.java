package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TilePerHandlerHeavyOre extends TileBaseHandlerHeavyOre {

    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;

    public TilePerHandlerHeavyOre(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PERFECT,BlockBaseMachine3.quad_handlerho,pos,state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.4));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quad_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
