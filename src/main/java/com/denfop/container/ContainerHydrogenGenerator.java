package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.fluid.TileHydrogenGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHydrogenGenerator extends ContainerFullInv<TileHydrogenGenerator> {

    public ContainerHydrogenGenerator(EntityPlayer entityPlayer, TileHydrogenGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 117, 49));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
