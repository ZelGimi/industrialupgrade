package com.denfop.container;

import com.denfop.tiles.mechanism.solardestiller.TileEntityBaseSolarDestiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSolarDestiller extends ContainerFullInv<TileEntityBaseSolarDestiller> {

    public ContainerSolarDestiller(EntityPlayer player, TileEntityBaseSolarDestiller tileEntite) {
        super(null, tileEntite, 171);
        this.addSlotToContainer(new SlotInvSlot(tileEntite.waterinputSlot, 0, 18, 28));
        this.addSlotToContainer(new SlotInvSlot(tileEntite.destiwaterinputSlot, 0, 142, 28));
        this.addSlotToContainer(new SlotInvSlot(tileEntite.wateroutputSlot, 0, 18, 46));
        this.addSlotToContainer(new SlotInvSlot(tileEntite.destiwateroutputSlott, 0, 142, 46));

        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntite.upgradeSlot, i, 71 + i * 18, 70));
        }
        final int width = 178;
        final int height = 171;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        player.inventory,
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + 1 + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, xStart + col * 18, height + -24));
        }
    }


}
