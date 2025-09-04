package com.denfop.containermenu;

import com.denfop.blockentity.reactors.heat.pump.BlockEntityBasePump;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatPump extends ContainerMenuFullInv<BlockEntityBasePump> {

    public ContainerMenuHeatPump(BlockEntityBasePump tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
