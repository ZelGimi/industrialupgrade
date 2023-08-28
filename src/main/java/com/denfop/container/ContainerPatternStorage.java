package com.denfop.container;

import com.denfop.tiles.mechanism.TilePatternStorage;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPatternStorage extends ContainerFullInv<TilePatternStorage> {

    public ContainerPatternStorage(EntityPlayer player, TilePatternStorage tileEntity1) {
        super(player, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 18, 20));
    }


}
