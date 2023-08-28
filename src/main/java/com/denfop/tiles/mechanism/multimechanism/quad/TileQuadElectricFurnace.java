package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadElectricFurnace extends TileMultiMachine {

    public TileQuadElectricFurnace() {
        super(
                EnumMultiMachine.QUAD_ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.QUAD_ELECTRIC_FURNACE.lenghtOperation,
                0
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.quad_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockElecFurnace3.name");
    }

    public String getStartSoundFile() {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


}
