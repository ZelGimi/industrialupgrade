package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;


public class TileDoubleRecycler extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileDoubleRecycler() {
        super(
                EnumMultiMachine.DOUBLE_RECYCLER.usagePerTick,
                EnumMultiMachine.DOUBLE_RECYCLER.lenghtOperation
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.double_recycler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
    }

    public void initiate(int soundEvent) {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRecycler.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
