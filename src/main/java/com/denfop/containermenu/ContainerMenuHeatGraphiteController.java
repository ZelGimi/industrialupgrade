package com.denfop.containermenu;


import com.denfop.blockentity.reactors.heat.graphite_controller.BlockEntityGraphiteController;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatGraphiteController extends ContainerMenuFullInv<BlockEntityGraphiteController> {

    public ContainerMenuHeatGraphiteController(BlockEntityGraphiteController tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger, 188, 210);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 86, 70));
    }

}
