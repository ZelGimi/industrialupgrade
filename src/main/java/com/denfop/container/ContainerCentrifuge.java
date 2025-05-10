package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityCentrifuge;
import net.minecraft.world.entity.player.Player;

public class ContainerCentrifuge extends ContainerFullInv<TileEntityCentrifuge> {

    public ContainerCentrifuge(final Player player, final TileEntityCentrifuge base) {
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


}
