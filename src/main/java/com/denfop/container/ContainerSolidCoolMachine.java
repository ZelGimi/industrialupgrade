package com.denfop.container;

import com.denfop.tiles.mechanism.TileSolidCooling;
import net.minecraft.world.entity.player.Player;

public class ContainerSolidCoolMachine extends ContainerFullInv<TileSolidCooling> {

    public ContainerSolidCoolMachine(Player entityPlayer, TileSolidCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.slot, 0, 100, 40));

    }


}
