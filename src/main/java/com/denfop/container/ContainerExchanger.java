package com.denfop.container;

import com.denfop.tiles.reactors.graphite.exchanger.TileEntityExchanger;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerExchanger extends ContainerFullInv<TileEntityExchanger> {

    public ContainerExchanger(TileEntityExchanger tileEntityExchanger, EntityPlayer var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
