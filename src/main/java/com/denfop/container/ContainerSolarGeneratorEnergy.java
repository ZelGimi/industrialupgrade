package com.denfop.container;

import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerSolarGeneratorEnergy extends ContainerFullInv<TileSolarGeneratorEnergy> {

    public ContainerSolarGeneratorEnergy(Player player, TileSolarGeneratorEnergy tileEntity) {
        super(null, tileEntity, 166);
        this.player = player;
        this.inventory = player.getInventory();
        addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 41, 30));
        for (int i = 0; i < tileEntity.input.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.input, i, 152, 9 + i * 18));
        }
        final int width = 178;
        final int height = 166;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        player.getInventory(),
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + 1 + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.getInventory(), col, xStart + col * 18, height + -24));
        }
    }


}
