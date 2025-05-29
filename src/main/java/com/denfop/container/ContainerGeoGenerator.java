package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerGeoGenerator extends ContainerFullInv<TileEntityGeoGenerator> {

    public ContainerGeoGenerator(Player entityPlayer, TileEntityGeoGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 117, 60));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
