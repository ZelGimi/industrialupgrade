package com.denfop.containermenu;

import com.denfop.blockentity.reactors.graphite.graphite_controller.BlockEntityGraphiteController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGraphiteController extends ContainerMenuFullInv<BlockEntityGraphiteController> {

    public ContainerMenuGraphiteController(BlockEntityGraphiteController tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 86, 70));
    }

}
