package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityCactusFarm;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCactusFarm extends ContainerFullInv<TileEntityCactusFarm> {

    public ContainerCactusFarm(TileEntityCactusFarm tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, i, 10 + (i % 9) * 18, 18 + 18 * (i / 9)));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
