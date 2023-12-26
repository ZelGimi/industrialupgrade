package com.denfop.container;

import com.denfop.tiles.reactors.gas.recirculation_pump.TileEntityBaseReCirculationPump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerReCirculationPump extends ContainerFullInv<TileEntityBaseReCirculationPump> {

    public ContainerReCirculationPump(TileEntityBaseReCirculationPump tileEntityBaseReCirculationPump, EntityPlayer var1) {
        super(var1,tileEntityBaseReCirculationPump,188,209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseReCirculationPump.getSlot(),0,85,50));
    }

}
