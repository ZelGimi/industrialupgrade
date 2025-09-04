package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityNuclearWasteRecycler;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuNuclearWasteRecycler extends ContainerMenuFullInv<BlockEntityNuclearWasteRecycler> {

    public ContainerMenuNuclearWasteRecycler(BlockEntityNuclearWasteRecycler tileEntityNuclearWasteRecycler, Player var1) {
        super(tileEntityNuclearWasteRecycler, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityNuclearWasteRecycler.inputSlotA, 0, 70, 17));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
