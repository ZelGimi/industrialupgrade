package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityAirCollector;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAirCollector extends ContainerMenuFullInv<BlockEntityAirCollector> {

    public ContainerMenuAirCollector(Player entityPlayer, BlockEntityAirCollector tileEntity1) {
        super(entityPlayer, tileEntity1, 202);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 15, 94));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 60, 94));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 2, 108, 94));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[0], 0, 15, 38));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[1], 0, 60, 38));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[2], 0, 108, 38));


        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, 152, 42 + i * 18));
        }

    }


}
