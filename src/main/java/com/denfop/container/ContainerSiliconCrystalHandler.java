package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySiliconCrystalHandler;
import net.minecraft.world.entity.player.Player;

public class ContainerSiliconCrystalHandler extends ContainerFullInv<TileEntitySiliconCrystalHandler> {

    public ContainerSiliconCrystalHandler(final Player player, final TileEntitySiliconCrystalHandler base) {
        super(player, base);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 50, 60));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        this.addSlotToContainer(new SlotInvSlot(base.flintSlot, 0, 30, 43));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
