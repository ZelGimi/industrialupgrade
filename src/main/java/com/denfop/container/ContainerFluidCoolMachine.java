package com.denfop.container;

import com.denfop.tiles.mechanism.cooling.TileFluidCooling;
import net.minecraft.world.entity.player.Player;

public class ContainerFluidCoolMachine extends ContainerFullInv<TileFluidCooling> {

    public ContainerFluidCoolMachine(Player entityPlayer, TileFluidCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.outputSlot, 0, 143, 60));
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.fluidSlot1, 0, 143, 20));

    }


}
