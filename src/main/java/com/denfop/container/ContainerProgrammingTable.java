package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityProgrammingTable;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerProgrammingTable extends ContainerFullInv<TileEntityProgrammingTable> {

    public ContainerProgrammingTable(TileEntityProgrammingTable tleEntityMatterFactory, EntityPlayer var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 30, 45));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 90, 45));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tleEntityMatterFactory).upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
