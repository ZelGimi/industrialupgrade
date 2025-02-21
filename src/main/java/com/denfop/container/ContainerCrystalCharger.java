package com.denfop.container;

import com.denfop.tiles.mechanism.TileCrystalCharger;
import com.denfop.tiles.mechanism.TileEntityLaserPolisher;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCrystalCharger extends ContainerFullInv<TileCrystalCharger> {

    public ContainerCrystalCharger(final EntityPlayer player, final TileCrystalCharger base) {
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
