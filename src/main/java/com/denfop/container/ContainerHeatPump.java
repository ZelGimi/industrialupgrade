package com.denfop.container;

import com.denfop.tiles.reactors.heat.pump.TileEntityBasePump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatPump extends ContainerFullInv<TileEntityBasePump> {

    public ContainerHeatPump(TileEntityBasePump tileEntityExchanger, EntityPlayer var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
