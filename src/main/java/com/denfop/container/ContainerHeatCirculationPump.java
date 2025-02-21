package com.denfop.container;

import com.denfop.tiles.reactors.heat.circulationpump.TileEntityBaseCirculationPump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatCirculationPump extends ContainerFullInv<TileEntityBaseCirculationPump> {

    public ContainerHeatCirculationPump(TileEntityBaseCirculationPump tileEntityExchanger, EntityPlayer var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
