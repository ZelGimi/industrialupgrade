package com.powerutils;


import com.denfop.container.ContainerFullInv;
import com.denfop.container.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerTEConverter extends ContainerFullInv<TileEntityTEConverter> {

    public ContainerTEConverter(EntityPlayer entityPlayer, TileEntityTEConverter tileEntity) {
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
        ret.add("maxStorage2");
        ret.add("capacity");
        ret.add("perenergy");
        ret.add("rf");
        ret.add("tier");
        ret.add("differenceenergy1");
        ret.add("differenceenergy");
        return ret;
    }

}
