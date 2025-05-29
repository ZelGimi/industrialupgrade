package com.denfop.container;

import com.denfop.tiles.reactors.gas.intercooler.TileEntityBaseInterCooler;
import net.minecraft.world.entity.player.Player;

public class ContainerInterCooler extends ContainerFullInv<TileEntityBaseInterCooler> {

    public ContainerInterCooler(TileEntityBaseInterCooler tileEntityBaseInterCooler, Player var1) {
        super(var1, tileEntityBaseInterCooler, 188, 209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseInterCooler.getSlot(), 0, 86, 51));

    }

}
