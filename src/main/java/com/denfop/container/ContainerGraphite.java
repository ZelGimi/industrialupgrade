package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityGraphiteHandler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphite extends ContainerFullInv<TileEntityGraphiteHandler> {

    public ContainerGraphite(final EntityPlayer player, final TileEntityGraphiteHandler base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 50, 60));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        this.addSlotToContainer(new SlotInvSlot(base.flintSlot, 0, 30, 43));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
