package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleFermer extends TileMultiMachine {

    public TileTripleFermer() {
        super(EnumMultiMachine.TRIPLE_Fermer.usagePerTick, EnumMultiMachine.TRIPLE_Fermer.lenghtOperation,
                3
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.triple_farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer2.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
