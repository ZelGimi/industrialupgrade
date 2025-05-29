package com.denfop.container;

import com.denfop.tiles.reactors.heat.circulationpump.TileEntityBaseCirculationPump;
import net.minecraft.world.entity.player.Player;

public class ContainerHeatCirculationPump extends ContainerFullInv<TileEntityBaseCirculationPump> {

    public ContainerHeatCirculationPump(TileEntityBaseCirculationPump tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
