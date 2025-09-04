package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.generator.energy.BlockEntityGeoGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGeoGenerator extends ContainerMenuFullInv<BlockEntityGeoGenerator> {

    public ContainerMenuGeoGenerator(Player entityPlayer, BlockEntityGeoGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 117, 60));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
