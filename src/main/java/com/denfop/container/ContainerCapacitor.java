package com.denfop.container;

import com.denfop.tiles.reactors.graphite.capacitor.TileEntityCapacitor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCapacitor extends ContainerFullInv<TileEntityCapacitor> {

    public ContainerCapacitor(TileEntityCapacitor tileEntityExchanger, EntityPlayer var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
