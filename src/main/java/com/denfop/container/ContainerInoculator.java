package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityInoculator;
import net.minecraft.world.entity.player.Player;

public class ContainerInoculator extends ContainerFullInv<TileEntityInoculator> {

    public ContainerInoculator(final Player player, final TileEntityInoculator base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 20, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
