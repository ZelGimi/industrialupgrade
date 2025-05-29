package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAutoOpenBox;
import net.minecraft.world.entity.player.Player;

public class ContainerAutoOpenBox extends ContainerFullInv<TileEntityAutoOpenBox> {

    public ContainerAutoOpenBox(TileEntityAutoOpenBox tileEntityAutoOpenBox, Player var1) {
        super(tileEntityAutoOpenBox, var1);
        for (int i = 0; i < 15; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityAutoOpenBox.slot, i, 40 + (i % 5) * 18, 28 + (i / 5) * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityAutoOpenBox.slot1, 0, 15, 46));
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityAutoOpenBox.upgradeSlot, i, 152, 26 + i * 18));
        }
    }

}
