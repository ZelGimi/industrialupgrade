package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.fluid.TileHydrogenGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerHydrogenGenerator extends ContainerFullInv<TileHydrogenGenerator> {

    public ContainerHydrogenGenerator(Player entityPlayer, TileHydrogenGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 27, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 27, 54));
    }


}
