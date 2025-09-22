package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityNeutronSeparator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNeutronSeparator extends ContainerFullInv<TileEntityNeutronSeparator> {


    public ContainerNeutronSeparator(final EntityPlayer player, final TileEntityNeutronSeparator base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
