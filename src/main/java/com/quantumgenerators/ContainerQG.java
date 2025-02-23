package com.quantumgenerators;


import com.denfop.container.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerQG extends ContainerFullInv<TileQuantumGenerator> {

    public ContainerQG(EntityPlayer entityPlayer, TileQuantumGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 196);


    }

    public ContainerQG(EntityPlayer entityPlayer, TileQuantumGenerator tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
