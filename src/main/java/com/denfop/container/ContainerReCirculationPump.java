package com.denfop.container;

import com.denfop.tiles.reactors.gas.recirculation_pump.TileEntityBaseReCirculationPump;
import net.minecraft.world.entity.player.Player;

public class ContainerReCirculationPump extends ContainerFullInv<TileEntityBaseReCirculationPump> {

    public ContainerReCirculationPump(TileEntityBaseReCirculationPump tileEntityBaseReCirculationPump, Player var1) {
        super(var1, tileEntityBaseReCirculationPump, 188, 209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseReCirculationPump.getSlot(), 0, 86, 51));
    }

}
