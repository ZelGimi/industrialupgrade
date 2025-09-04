package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntitySolidCooling;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSolidCoolMachine extends ContainerMenuFullInv<BlockEntitySolidCooling> {

    public ContainerMenuSolidCoolMachine(Player entityPlayer, BlockEntitySolidCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.slot, 0, 100, 40));

    }


}
