package com.denfop.container;

import com.denfop.tiles.mechanism.blastfurnace.block.TileEntityBlastFurnaceMain;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerBlastFurnace extends ContainerFullInv<TileEntityBlastFurnaceMain> {

    public ContainerBlastFurnace(EntityPlayer entityPlayer, TileEntityBlastFurnaceMain tileEntityBlastFurnaceMain) {
        super(entityPlayer, tileEntityBlastFurnaceMain, 166);
        if (tileEntityBlastFurnaceMain.blastOutputItem != null) {
            addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.blastOutputItem.getOutput(), 0,
                    116, 35
            ));
        }
        if (tileEntityBlastFurnaceMain.blastInputItem != null) {
            addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.blastInputItem.getInput(), 0,
                    56, 34
            ));
        }
        if (tileEntityBlastFurnaceMain.blastInputItem != null) {
            addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.getInputFluid().getInvSlotOutput(),
                    0, 29, 62
            ));
            addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.getInputFluid().getInvSlotConsumableLiquidBy(),
                    0, 8, 62
            ));
        }
    }

    @Override
    public void onContainerClosed(@Nonnull final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.base.entityPlayerList.remove(playerIn);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("tank");
        ret.add("full");
        if (this.base.blastHeat != null) {
            ret.add("component");
        }
        if (this.base.blastInputFluid != null) {
            ret.add("tank1");
        }
        if (this.base.blastInputItem != null) {
            ret.add("blastInputItem");
        }
        if (this.base.blastOutputItem != null) {
            ret.add("blastOutputItem");
        }
        ret.add("progress");
        ret.add("bar");
        ret.add("sound");
        return ret;

    }

}
