package com.denfop.containermenu;

import com.denfop.blockentity.reactors.graphite.capacitor.BlockEntityCapacitor;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCapacitor extends ContainerMenuFullInv<BlockEntityCapacitor> {

    public ContainerMenuCapacitor(BlockEntityCapacitor tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
