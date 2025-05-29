package com.denfop.container;

import com.denfop.tiles.mechanism.TilePatternStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerPatternStorage extends ContainerFullInv<TilePatternStorage> {

    public ContainerPatternStorage(Player player, TilePatternStorage tileEntity1) {
        super(player, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 18, 20));
    }


}
