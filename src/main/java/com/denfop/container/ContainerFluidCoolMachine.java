package com.denfop.container;

import com.denfop.tiles.mechanism.cooling.TileFluidCooling;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFluidCoolMachine extends ContainerFullInv<TileFluidCooling> {

    public ContainerFluidCoolMachine(EntityPlayer entityPlayer, TileFluidCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.outputSlot, 0, 143, 60));
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.fluidSlot1, 0, 143, 20));

    }


}
