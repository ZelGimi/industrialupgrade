package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWeeder;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWeeder extends ContainerFullInv<TileEntityWeeder> {

    public ContainerWeeder(TileEntityWeeder tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 80, 18));
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
