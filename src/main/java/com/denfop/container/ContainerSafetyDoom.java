package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityReactorSafetyDoom;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSafetyDoom extends ContainerFullInv<TileEntityReactorSafetyDoom> {

    public ContainerSafetyDoom(TileEntityReactorSafetyDoom tileEntityReactorSafetyDoom, EntityPlayer entityPlayer) {
        super(tileEntityReactorSafetyDoom, entityPlayer);
    }

}
