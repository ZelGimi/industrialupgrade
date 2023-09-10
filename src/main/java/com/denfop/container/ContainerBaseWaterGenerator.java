package com.denfop.container;

import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBaseWaterGenerator extends ContainerFullInv<TileBaseWaterGenerator> {

    public ContainerBaseWaterGenerator(TileBaseWaterGenerator windGenerator, EntityPlayer entityPlayer) {
        super(entityPlayer, windGenerator, 236);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 89, 19));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 30, 19));

    }


}
