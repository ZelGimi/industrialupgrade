package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.radiation_storage.BlockEntityRadiationStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRadiationStorage extends ContainerMenuFullInv<BlockEntityRadiationStorage> {

    public ContainerMenuRadiationStorage(Player entityPlayer, BlockEntityRadiationStorage tileEntityRadiationStorage) {
        super(entityPlayer, tileEntityRadiationStorage, 166);
    }

}
