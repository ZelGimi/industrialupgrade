package com.denfop.container;

import com.denfop.tiles.mechanism.TileMagnetGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMagnetGenerator extends ContainerFullInv<TileMagnetGenerator> {

    public ContainerMagnetGenerator(EntityPlayer entityPlayer, TileMagnetGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerMagnetGenerator(EntityPlayer entityPlayer, TileMagnetGenerator tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
