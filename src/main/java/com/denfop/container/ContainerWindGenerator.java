package com.denfop.container;

import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWindGenerator extends ContainerFullInv<TileWindGenerator> {

    public ContainerWindGenerator(TileWindGenerator windGenerator, EntityPlayer entityPlayer) {
        super(entityPlayer, windGenerator, 246);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 89, 19));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 30, 19));

    }


}
