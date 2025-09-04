package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityMagnetGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuMagnetGenerator extends ContainerMenuFullInv<BlockEntityMagnetGenerator> {

    public ContainerMenuMagnetGenerator(Player entityPlayer, BlockEntityMagnetGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerMenuMagnetGenerator(Player entityPlayer, BlockEntityMagnetGenerator tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
