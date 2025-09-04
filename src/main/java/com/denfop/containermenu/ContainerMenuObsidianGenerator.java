package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityBaseObsidianGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerMenuObsidianGenerator extends ContainerMenuFullInv<BlockEntityBaseObsidianGenerator> {

    public ContainerMenuObsidianGenerator(Player entityPlayer, BlockEntityBaseObsidianGenerator tileEntity1) {
        super(null, tileEntity1, 166);
        this.player = entityPlayer;
        this.inventory = entityPlayer.getInventory();
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 127, 41));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1, 0, 23, 59));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 23, 19));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 23, 39));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 9 + i * 18
            ));
        }
        final int width = 178;
        int height = 167;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        entityPlayer.getInventory(),
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.getInventory(), col, xStart + col * 18, -1 + height + -24));
        }
    }


}
