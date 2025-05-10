package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRadioactiveOreHandler;
import net.minecraft.world.entity.player.Player;

public class ContainerRadioactiveOreHandler extends ContainerFullInv<TileEntityRadioactiveOreHandler> {

    public ContainerRadioactiveOreHandler(final Player player, final TileEntityRadioactiveOreHandler base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot1, 0, 110, 55));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }

    }

}
