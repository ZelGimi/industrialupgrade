package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityRadioactiveOreHandler;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRadioactiveOreHandler extends ContainerMenuFullInv<BlockEntityRadioactiveOreHandler> {

    public ContainerMenuRadioactiveOreHandler(final Player player, final BlockEntityRadioactiveOreHandler base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot1, 0, 110, 55));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }

    }

}
