package com.denfop.container;

import com.denfop.tiles.mechanism.solarium_storage.TileEntitySolariumStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerSolariumStorage extends ContainerFullInv<TileEntitySolariumStorage> {

    public ContainerSolariumStorage(Player entityPlayer, TileEntitySolariumStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
