package com.denfop.container;

import com.denfop.tiles.mechanism.TileMagnetGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMagnetGenerator extends ContainerFullInv<TileMagnetGenerator> {

    public ContainerMagnetGenerator(Player entityPlayer, TileMagnetGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerMagnetGenerator(Player entityPlayer, TileMagnetGenerator tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
