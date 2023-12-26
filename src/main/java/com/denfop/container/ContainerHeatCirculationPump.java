package com.denfop.container;

import com.denfop.container.ContainerBase;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntityExchanger;
import com.denfop.tiles.reactors.heat.circulationpump.TileEntityBaseCirculationPump;
import com.denfop.tiles.reactors.heat.pump.TileEntityBasePump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatCirculationPump extends ContainerFullInv<TileEntityBaseCirculationPump> {

    public ContainerHeatCirculationPump(TileEntityBaseCirculationPump tileEntityExchanger, EntityPlayer var1) {
        super(var1,tileEntityExchanger,188,209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(),0,84,58));
    }

}
