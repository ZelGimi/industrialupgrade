package com.denfop.container;


import com.denfop.tiles.reactors.heat.graphite_controller.TileEntityGraphiteController;
import net.minecraft.world.entity.player.Player;

public class ContainerHeatGraphiteController extends ContainerFullInv<TileEntityGraphiteController> {

    public ContainerHeatGraphiteController(TileEntityGraphiteController tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 86, 70));
    }

}
