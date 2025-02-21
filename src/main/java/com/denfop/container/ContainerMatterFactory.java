package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityMatterFactory;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMatterFactory extends ContainerFullInv<TileEntityMatterFactory> {

    public ContainerMatterFactory(TileEntityMatterFactory tileEntityMatterFactory, EntityPlayer var1) {
        super(tileEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityMatterFactory.inputSlotA, 0, 70, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntityMatterFactory.outputSlot, 0, 70, 60));
    }

}
