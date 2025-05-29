package com.denfop.container;

import com.denfop.tiles.mechanism.TileWireInsulator;
import net.minecraft.world.entity.player.Player;

public class ContainerWireInsulator extends ContainerFullInv<TileWireInsulator> {

    public ContainerWireInsulator(final Player player, final TileWireInsulator base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50 - 30, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 46, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
        addSlotToContainer(new SlotInvSlot(base.input_slot,
                0, -20, 84
        ));
    }

    public ContainerWireInsulator(final Player player, final TileWireInsulator base, boolean false1) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50 - 30, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 46, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
