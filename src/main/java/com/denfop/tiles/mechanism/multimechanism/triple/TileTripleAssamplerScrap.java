package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleAssamplerScrap extends TileMultiMachine {

    public TileTripleAssamplerScrap() {
        super(
                EnumMultiMachine.TRIPLE_AssamplerScrap.usagePerTick,
                EnumMultiMachine.TRIPLE_AssamplerScrap.lenghtOperation,
                3
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.triple_assamplerscrap;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap2.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
