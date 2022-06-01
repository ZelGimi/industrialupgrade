package com.powerutils;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerQEConverter extends ContainerFullInv<TileEntityQEConverter> {

    public ContainerQEConverter(EntityPlayer entityPlayer, TileEntityQEConverter tileEntity) {
        super(entityPlayer, tileEntity, 176);
        for (int k = 0; k < 4; k++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, k, 152, 17 + k * 18));
        }
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        this.base.list.remove(playerIn);
        super.onContainerClosed(playerIn);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy2");
        ret.add("energy");
        ret.add("rf");
        ret.add("tier");
        ret.add("capacity");
        ret.add("capacity2");
        ret.add("differenceenergy1");
        ret.add("differenceenergy");
        return ret;
    }

}
