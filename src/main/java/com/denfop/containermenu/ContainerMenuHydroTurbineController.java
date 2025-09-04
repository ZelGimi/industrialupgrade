package com.denfop.containermenu;

import com.denfop.blockentity.hydroturbine.BlockEntityHydroTurbineController;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHydroTurbineController extends ContainerMenuFullInv<BlockEntityHydroTurbineController> {

    public ContainerMenuHydroTurbineController(BlockEntityHydroTurbineController windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 208, 242);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 59, 135));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 28, 135));

    }


}
