package com.denfop.container;

import com.denfop.tiles.hydroturbine.TileEntityHydroTurbineController;
import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHydroTurbineController extends ContainerFullInv<TileEntityHydroTurbineController> {

    public ContainerHydroTurbineController(TileEntityHydroTurbineController windGenerator, EntityPlayer entityPlayer) {
        super(entityPlayer, windGenerator, 246);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 89, 19));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 30, 19));

    }


}
