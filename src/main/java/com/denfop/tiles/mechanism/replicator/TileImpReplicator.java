package com.denfop.tiles.mechanism.replicator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.mechanism.TileBaseReplicator;

public class TileImpReplicator extends TileBaseReplicator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileImpReplicator() {
        super(0.85);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_replicator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
