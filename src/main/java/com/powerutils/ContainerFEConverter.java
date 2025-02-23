package com.powerutils;


import com.denfop.container.ContainerFullInv;
import com.denfop.container.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFEConverter extends ContainerFullInv<TileFEConverter> {

    public ContainerFEConverter(EntityPlayer entityPlayer, TileFEConverter tileEntity) {
        super(entityPlayer, tileEntity, 176,183);
        for (int k = 0; k < 4; k++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, k, 151, 19 + k * 18));
        }
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        this.base.list.remove(playerIn);
        super.onContainerClosed(playerIn);
    }


}
