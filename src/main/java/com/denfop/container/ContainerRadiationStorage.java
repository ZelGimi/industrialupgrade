package com.denfop.container;

import com.denfop.tiles.mechanism.radiation_storage.TileEntityRadiationStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerRadiationStorage extends ContainerFullInv<TileEntityRadiationStorage> {

    public ContainerRadiationStorage(Player entityPlayer, TileEntityRadiationStorage tileEntityRadiationStorage) {
        super(entityPlayer, tileEntityRadiationStorage, 166);
    }

}
