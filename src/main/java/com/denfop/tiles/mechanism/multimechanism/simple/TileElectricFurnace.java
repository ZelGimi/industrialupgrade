package com.denfop.tiles.mechanism.multimechanism.simple;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileElectricFurnace extends TileMultiMachine {

    public TileElectricFurnace() {
        super(
                EnumMultiMachine.ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.ELECTRIC_FURNACE.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSimpleMachine.furnace_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockElecFurnace.name");
    }

    public String getStartSoundFile() {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


}
