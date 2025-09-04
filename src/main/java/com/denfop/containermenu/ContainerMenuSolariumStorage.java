package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.solarium_storage.BlockEntitySolariumStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSolariumStorage extends ContainerMenuFullInv<BlockEntitySolariumStorage> {

    public ContainerMenuSolariumStorage(Player entityPlayer, BlockEntitySolariumStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
