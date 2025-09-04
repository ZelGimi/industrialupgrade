package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityBioFuelGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBiomassGenerator extends ContainerMenuFullInv<BlockEntityBioFuelGenerator> {

    public ContainerMenuBiomassGenerator(Player entityPlayer, BlockEntityBioFuelGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
