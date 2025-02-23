package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityChickenFarm;
import com.denfop.tiles.mechanism.TileEntityCowFarm;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCowFarm extends ContainerFullInv<TileEntityCowFarm> {

    public ContainerCowFarm(TileEntityCowFarm tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slotSeeds, 0, 80, 18));
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output, i, 10 + i * 18, 45));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output1, i, 10 + i * 18, 45+18+2));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 20 + (i+4) * 18, 45+18+2));
        }
    }

}
