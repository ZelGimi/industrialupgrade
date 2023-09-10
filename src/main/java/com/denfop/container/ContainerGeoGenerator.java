package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGeoGenerator extends ContainerFullInv<TileEntityGeoGenerator> {

    public ContainerGeoGenerator(EntityPlayer entityPlayer, TileEntityGeoGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 117, 49));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
