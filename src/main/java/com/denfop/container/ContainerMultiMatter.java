package com.denfop.container;


import com.denfop.tiles.base.TileMultiMatter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMultiMatter extends ContainerFullInv<TileMultiMatter> {

    public ContainerMultiMatter(EntityPlayer entityPlayer, TileMultiMatter tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.amplifierSlot, 0, 72, 40));
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
