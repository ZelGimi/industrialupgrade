package com.denfop.container;

import com.denfop.tiles.windturbine.TileEntityWindTurbineController;
import net.minecraft.world.entity.player.Player;

public class ContainerWindTurbine extends ContainerFullInv<TileEntityWindTurbineController> {

    public ContainerWindTurbine(TileEntityWindTurbineController windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 208, 242);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 59, 135));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 28, 135));

    }


}
