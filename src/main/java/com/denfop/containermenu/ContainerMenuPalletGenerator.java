package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPalletGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPalletGenerator extends ContainerMenuFullInv<BlockEntityPalletGenerator> {

    public ContainerMenuPalletGenerator(BlockEntityPalletGenerator tileEntityPalletGenerator, Player var1) {
        super(tileEntityPalletGenerator, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityPalletGenerator.slot, 0, 50, 18));
        this.addSlotToContainer(new SlotInvSlot(tileEntityPalletGenerator.slot, 1, 68, 18));
        this.addSlotToContainer(new SlotInvSlot(tileEntityPalletGenerator.slot, 2, 86, 18));

        this.addSlotToContainer(new SlotInvSlot(tileEntityPalletGenerator.slot, 3, 50, 36));
        this.addSlotToContainer(new SlotInvSlot(tileEntityPalletGenerator.slot, 4, 68, 36));
        this.addSlotToContainer(new SlotInvSlot(tileEntityPalletGenerator.slot, 5, 86, 36));
    }

}
