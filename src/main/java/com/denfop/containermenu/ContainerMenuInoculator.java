package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityInoculator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuInoculator extends ContainerMenuFullInv<BlockEntityInoculator> {

    public ContainerMenuInoculator(final Player player, final BlockEntityInoculator base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 20, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
