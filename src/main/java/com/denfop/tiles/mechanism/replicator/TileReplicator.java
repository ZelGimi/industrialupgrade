package com.denfop.tiles.mechanism.replicator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.mechanism.TileBaseReplicator;

public class TileReplicator extends TileBaseReplicator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileReplicator() {
        super(1);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.replicator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
