package com.denfop.container;

import com.denfop.tiles.reactors.graphite.exchanger.TileEntityExchanger;
import net.minecraft.world.entity.player.Player;

public class ContainerExchanger extends ContainerFullInv<TileEntityExchanger> {

    public ContainerExchanger(TileEntityExchanger tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 84, 58));
    }

}
