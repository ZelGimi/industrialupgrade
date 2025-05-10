package com.denfop.container;

import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerSolidMatter extends ContainerFullInv<TileEntityMatterGenerator> {

    public ContainerSolidMatter(Player entityPlayer, TileEntityMatterGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 167, 152, 8);
    }

    public ContainerSolidMatter(
            Player entityPlayer,
            TileEntityMatterGenerator tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(null, tileEntity1, height);
        this.inventory = entityPlayer.getInventory();
        this.player = entityPlayer;
        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    0, 80, 26
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18 + 1
            ));
        }
        final int width = 178;
        height = 167;
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
