package com.denfop.container;

import com.denfop.tiles.base.TileEntityBaseObsidianGenerator;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerObsidianGenerator extends ContainerFullInv<TileEntityBaseObsidianGenerator> {

    public ContainerObsidianGenerator(EntityPlayer entityPlayer, TileEntityBaseObsidianGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 131, 34));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1, 0, 55, 65));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 15, 10));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 15, 34));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("fluidTank1");
        ret.add("fluidTank2");
        ret.add("energy");
        ret.add("guiProgress");
        ret.add("guiChargeLevel");
        return ret;
    }

}
