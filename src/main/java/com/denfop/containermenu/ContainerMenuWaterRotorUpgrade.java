package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityWaterRotorModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;

public class ContainerMenuWaterRotorUpgrade extends ContainerMenuFullInv<BlockEntityWaterRotorModifier> {

    public ContainerMenuWaterRotorUpgrade(BlockEntityWaterRotorModifier tileEntityRotorModifier, Player entityPlayer) {
        super(entityPlayer, tileEntityRotorModifier);
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                0, 80, 9
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                1, 53, 36
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                2, 107, 36
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                3, 80, 63
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.rotor_slot,
                0, 80, 36
        ));
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (clickType != ClickType.SWAP)
            super.clicked(slotId, dragType, clickType, player);
    }
}
