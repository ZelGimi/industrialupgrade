package com.denfop.container;

import com.denfop.tiles.base.TileRadiationPurifier;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRadiationPurifier extends ContainerFullInv<TileRadiationPurifier> {

    public ContainerRadiationPurifier(TileRadiationPurifier tileRadiationPurifier, EntityPlayer entityPlayer) {
        super(tileRadiationPurifier, entityPlayer);
        this.addSlotToContainer(new SlotInvSlot(tileRadiationPurifier.outputSlot, 0, 79, 37));
    }

}
