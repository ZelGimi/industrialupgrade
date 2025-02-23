package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAlkalineEarthQuarry;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAlkalineEarthQuarry extends ContainerFullInv<TileEntityAlkalineEarthQuarry> {

    public ContainerAlkalineEarthQuarry(TileEntityAlkalineEarthQuarry tleEntityMatterFactory, EntityPlayer var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 30, 45));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotB, 0, 60, 64));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 90, 45));
    }

}
