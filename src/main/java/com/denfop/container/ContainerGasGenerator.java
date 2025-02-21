package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.fluid.TileGasGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasGenerator extends ContainerFullInv<TileGasGenerator> {

    public ContainerGasGenerator(EntityPlayer entityPlayer, TileGasGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
