package com.denfop.container;

import com.denfop.tiles.base.TileBaseWitherMaker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerBaseWitherMaker
        extends ContainerFullInv<TileBaseWitherMaker> {

    public ContainerBaseWitherMaker(Player entityPlayer, TileBaseWitherMaker tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerBaseWitherMaker(
            Player entityPlayer,
            TileBaseWitherMaker tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(null, tileEntity1, height);
        this.player = entityPlayer;
        this.inventory = player.getInventory();
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 8, 27));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 1, 26, 27));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 2, 44, 27));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 3, 8, 45));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 4, 26, 45));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 5, 44, 45));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 6, 26, 63));
        }
        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 120, 36));
        }

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, upgradeX, upgradeY + 1 + i * 18));
        }
        final int width = 178;
        height = 166;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        entityPlayer.getInventory(),
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + 1 + -82 + col * 18
                ));
            }
        }
        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.getInventory(), col, xStart + col * 18, height + -24));
        }
    }


}
