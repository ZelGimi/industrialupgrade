package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityAdvGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerGenerator extends ContainerFullInv<TileEntityAdvGenerator> {

    public ContainerGenerator(Player entityPlayer, TileEntityAdvGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fuelSlot, 0, 65, 53));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.chargeSlot, 0, 65, 17));
    }


}
