package com.denfop.container;

import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerBaseWaterGenerator extends ContainerFullInv<TileBaseWaterGenerator> {

    public ContainerBaseWaterGenerator(TileBaseWaterGenerator windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 246);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 89, 19));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 30, 19));

    }


}
