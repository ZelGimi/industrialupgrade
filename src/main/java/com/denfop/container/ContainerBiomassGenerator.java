package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.fluid.TileBioFuelGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerBiomassGenerator extends ContainerFullInv<TileBioFuelGenerator> {

    public ContainerBiomassGenerator(Player entityPlayer, TileBioFuelGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
