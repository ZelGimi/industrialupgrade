package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadFermer extends TileMultiMachine {

    public TileQuadFermer() {
        super(EnumMultiMachine.QUAD_Fermer.usagePerTick, EnumMultiMachine.QUAD_Fermer.lenghtOperation,
                3
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.quad_farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer3.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
