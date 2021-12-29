package com.denfop.container;

import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.ContainerFullInv;
import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMultiMachine extends ContainerFullInv<TileEntityMultiMachine> {

    public ContainerMultiMachine(EntityPlayer entityPlayer, TileEntityMultiMachine tileEntity1, int sizeWorkingSlot) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            int xDisplayPosition1 = 80 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
            addSlotToContainer(new SlotInvSlot((InvSlot) tileEntity1.inputSlots, i,
                    xDisplayPosition1, 16
            ));
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i,
                    xDisplayPosition1, 60
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.dischargeSlot, 0, 8, 63));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("expstorage");
        ret.add("energy2");
        ret.add("maxEnergy2");
        ret.add("energy");
        return ret;
    }

}
