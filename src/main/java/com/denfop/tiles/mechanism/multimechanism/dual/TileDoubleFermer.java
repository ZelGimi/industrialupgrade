package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileDoubleFermer extends TileMultiMachine {

    public TileDoubleFermer() {
        super(EnumMultiMachine.DOUBLE_Fermer.usagePerTick, EnumMultiMachine.DOUBLE_Fermer.lenghtOperation,
                3
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.double_farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer1.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
