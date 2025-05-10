package com.denfop.container;

import com.denfop.tiles.base.TileRadiationPurifier;
import net.minecraft.world.entity.player.Player;

public class ContainerRadiationPurifier extends ContainerFullInv<TileRadiationPurifier> {

    public ContainerRadiationPurifier(TileRadiationPurifier tileRadiationPurifier, Player entityPlayer) {
        super(tileRadiationPurifier, entityPlayer);
        this.addSlotToContainer(new SlotInvSlot(tileRadiationPurifier.outputSlot, 0, 79, 37));
    }

}
