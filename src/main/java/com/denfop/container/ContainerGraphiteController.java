package com.denfop.container;

import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityGraphiteController;
import net.minecraft.world.entity.player.Player;

public class ContainerGraphiteController extends ContainerFullInv<TileEntityGraphiteController> {

    public ContainerGraphiteController(TileEntityGraphiteController tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 86, 70));
    }

}
