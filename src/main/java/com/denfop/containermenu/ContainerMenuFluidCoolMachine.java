package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.cooling.BlockEntityFluidCooling;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFluidCoolMachine extends ContainerMenuFullInv<BlockEntityFluidCooling> {

    public ContainerMenuFluidCoolMachine(Player entityPlayer, BlockEntityFluidCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.outputSlot, 0, 143, 60));
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.fluidSlot1, 0, 143, 20));

    }


}
