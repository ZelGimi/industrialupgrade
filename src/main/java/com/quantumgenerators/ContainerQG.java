package com.quantumgenerators;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerQG extends ContainerFullInv<TileEntityQuantumGenerator> {

    public ContainerQG(EntityPlayer entityPlayer, TileEntityQuantumGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 196);



    }

    public ContainerQG(EntityPlayer entityPlayer, TileEntityQuantumGenerator  tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
       ret.add("energy");
       ret.add("gen");
        return ret;
    }


}
