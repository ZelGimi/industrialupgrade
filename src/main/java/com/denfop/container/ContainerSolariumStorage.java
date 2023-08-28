package com.denfop.container;

import com.denfop.tiles.mechanism.solarium_storage.TileEntitySolariumStorage;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolariumStorage extends ContainerFullInv<TileEntitySolariumStorage> {

    public ContainerSolariumStorage(EntityPlayer entityPlayer, TileEntitySolariumStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
