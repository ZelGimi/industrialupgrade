package com.denfop.container;

import com.denfop.tiles.mechanism.quantum_storage.TileEntityQuantumStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerQuantumStorage extends ContainerFullInv<TileEntityQuantumStorage> {

    public ContainerQuantumStorage(Player entityPlayer, TileEntityQuantumStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
