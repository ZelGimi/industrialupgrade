package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityChickenFarm;
import com.denfop.tiles.mechanism.TileEntityCowFarm;
import com.denfop.tiles.mechanism.TileEntitySheepFarm;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSheepFarm extends ContainerFullInv<TileEntitySheepFarm> {

    public ContainerSheepFarm(TileEntitySheepFarm tileEntityChickenFarm, EntityPlayer var1) {
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
