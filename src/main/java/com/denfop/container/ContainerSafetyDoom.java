package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityReactorSafetyDoom;
import net.minecraft.world.entity.player.Player;

public class ContainerSafetyDoom extends ContainerFullInv<TileEntityReactorSafetyDoom> {

    public ContainerSafetyDoom(TileEntityReactorSafetyDoom tileEntityReactorSafetyDoom, Player entityPlayer) {
        super(tileEntityReactorSafetyDoom, entityPlayer);
    }

}
