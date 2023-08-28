package com.denfop.container;


import com.denfop.tiles.base.TileNeutronGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNeutronGenerator extends ContainerFullInv<TileNeutronGenerator> {

    public ContainerNeutronGenerator(EntityPlayer entityPlayer, TileNeutronGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
