package com.denfop.container;

import com.denfop.tiles.mechanism.TileSolidCooling;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolidCoolMachine extends ContainerFullInv<TileSolidCooling> {

    public ContainerSolidCoolMachine(EntityPlayer entityPlayer, TileSolidCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.slot, 0, 100, 40));

    }


}
