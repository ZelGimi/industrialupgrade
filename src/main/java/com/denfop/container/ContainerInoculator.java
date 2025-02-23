package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBrewingPlant;
import com.denfop.tiles.mechanism.TileEntityCentrifuge;
import com.denfop.tiles.mechanism.TileEntityInoculator;
import com.denfop.tiles.mechanism.TileEntityLaserPolisher;
import com.denfop.tiles.mechanism.TileEntitySawmill;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerInoculator extends ContainerFullInv<TileEntityInoculator> {

    public ContainerInoculator(final EntityPlayer player, final TileEntityInoculator base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 20, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }



}
