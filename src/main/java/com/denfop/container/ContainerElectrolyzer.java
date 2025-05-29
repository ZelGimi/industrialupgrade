package com.denfop.container;

import com.denfop.tiles.base.TileElectrolyzer;
import net.minecraft.world.entity.player.Player;

public class ContainerElectrolyzer extends ContainerFullInv<TileElectrolyzer> {

    public ContainerElectrolyzer(Player entityPlayer, TileElectrolyzer tileEntity1) {
        super(entityPlayer, tileEntity1, 202);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 15, 8));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 133, 23));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 133, 83));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 100, 8));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 100, 100));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, 152, 26 + i * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.cathodeslot, 0, 40, 27));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.anodeslot, 0, 40, 101));

    }


}
