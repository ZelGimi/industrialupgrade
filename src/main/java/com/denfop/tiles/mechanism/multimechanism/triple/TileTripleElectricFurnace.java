package com.denfop.tiles.mechanism.multimechanism.triple;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleElectricFurnace extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileTripleElectricFurnace() {
        super(
                EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE.lenghtOperation
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.triple_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockElecFurnace2.name");
    }

    public String getStartSoundFile() {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


}
