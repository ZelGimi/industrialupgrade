package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityBaseHeatMachine;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatMachine extends ContainerMenuFullInv<BlockEntityBaseHeatMachine> {

    public ContainerMenuHeatMachine(Player entityPlayer, BlockEntityBaseHeatMachine tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        if (tileEntityBaseHeatMachine.hasFluid) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.fluidSlot, 0, 125, 23));
            this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.outputSlot, 0, 125, 59));
        }

    }


}
