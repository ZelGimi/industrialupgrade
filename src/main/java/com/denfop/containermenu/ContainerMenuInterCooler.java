package com.denfop.containermenu;

import com.denfop.blockentity.reactors.gas.intercooler.BlockEntityBaseInterCooler;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuInterCooler extends ContainerMenuFullInv<BlockEntityBaseInterCooler> {

    public ContainerMenuInterCooler(BlockEntityBaseInterCooler tileEntityBaseInterCooler, Player var1) {
        super(var1, tileEntityBaseInterCooler, 188, 209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseInterCooler.getSlot(), 0, 86, 51));

    }

}
