package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadAssamplerScrap extends TileMultiMachine {

    public TileQuadAssamplerScrap() {
        super(
                EnumMultiMachine.QUAD_AssamplerScrap.usagePerTick,
                EnumMultiMachine.QUAD_AssamplerScrap.lenghtOperation,
                3
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.quad_assamplerscrap;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap3.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
