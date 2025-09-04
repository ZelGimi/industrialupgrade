package com.denfop.containermenu;

import com.denfop.blockentity.reactors.gas.recirculation_pump.BlockEntityBaseReCirculationPump;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuReCirculationPump extends ContainerMenuFullInv<BlockEntityBaseReCirculationPump> {

    public ContainerMenuReCirculationPump(BlockEntityBaseReCirculationPump tileEntityBaseReCirculationPump, Player var1) {
        super(var1, tileEntityBaseReCirculationPump, 188, 209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseReCirculationPump.getSlot(), 0, 86, 51));
    }

}
