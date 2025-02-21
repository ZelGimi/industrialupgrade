package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.fluid.TileDieselGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDieselGenerator extends ContainerFullInv<TileDieselGenerator> {

    public ContainerDieselGenerator(EntityPlayer entityPlayer, TileDieselGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
