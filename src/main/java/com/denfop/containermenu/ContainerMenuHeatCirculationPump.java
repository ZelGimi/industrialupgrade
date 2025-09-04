package com.denfop.containermenu;

import com.denfop.blockentity.reactors.heat.circulationpump.BlockEntityBaseCirculationPump;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatCirculationPump extends ContainerMenuFullInv<BlockEntityBaseCirculationPump> {

    public ContainerMenuHeatCirculationPump(BlockEntityBaseCirculationPump tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
