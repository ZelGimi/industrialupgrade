package com.denfop.container;

import com.denfop.tiles.mechanism.radiation_storage.TileEntityRadiationStorage;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRadiationStorage extends ContainerFullInv<TileEntityRadiationStorage> {

    public ContainerRadiationStorage(EntityPlayer entityPlayer, TileEntityRadiationStorage tileEntityRadiationStorage) {
        super(entityPlayer, tileEntityRadiationStorage, 166);
    }

}
