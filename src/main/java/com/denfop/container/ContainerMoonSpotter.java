package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityMoonSpotter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMoonSpotter extends ContainerFullInv<TileEntityMoonSpotter>{

    public ContainerMoonSpotter(TileEntityMoonSpotter tleEntityMatterFactory, EntityPlayer var1) {
        super(tleEntityMatterFactory,var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 70, 17 ));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 70, 60 ));
    }

}
