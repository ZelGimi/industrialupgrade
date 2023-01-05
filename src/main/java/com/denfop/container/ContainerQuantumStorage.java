package com.denfop.container;

import com.denfop.tiles.mechanism.quantum_storage.TileEntityQuantumStorage;
import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerQuantumStorage extends ContainerFullInv<TileEntityQuantumStorage> {

    public ContainerQuantumStorage(EntityPlayer entityPlayer, TileEntityQuantumStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
