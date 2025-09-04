package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityLaserPolisher;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuLaserPolisher extends ContainerMenuFullInv<BlockEntityLaserPolisher> {

    public ContainerMenuLaserPolisher(final Player player, final BlockEntityLaserPolisher base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
        addSlotToContainer(new SlotInvSlot(base.input_slot,
                0, -20, 84
        ));
    }

    public ContainerMenuLaserPolisher(final Player player, final BlockEntityLaserPolisher base, boolean false1) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
