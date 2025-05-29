package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPigFarm;
import net.minecraft.world.entity.player.Player;

public class ContainerPigFarm extends ContainerFullInv<TileEntityPigFarm> {

    public ContainerPigFarm(TileEntityPigFarm tileEntityChickenFarm, Player var1) {
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
