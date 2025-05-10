package com.denfop.container;

import com.denfop.tiles.base.TileBaseHeatMachine;
import net.minecraft.world.entity.player.Player;

public class ContainerHeatMachine extends ContainerFullInv<TileBaseHeatMachine> {

    public ContainerHeatMachine(Player entityPlayer, TileBaseHeatMachine tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        if (tileEntityBaseHeatMachine.hasFluid) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.fluidSlot, 0, 125, 23));
            this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.outputSlot, 0, 125, 59));
        }

    }


}
