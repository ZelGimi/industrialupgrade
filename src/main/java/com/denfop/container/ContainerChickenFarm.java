package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityChickenFarm;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChickenFarm extends ContainerFullInv<TileEntityChickenFarm> {

    public ContainerChickenFarm(TileEntityChickenFarm tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slotSeeds, 0, 80, 18));
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output, i, 10 + i * 18, 45));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
