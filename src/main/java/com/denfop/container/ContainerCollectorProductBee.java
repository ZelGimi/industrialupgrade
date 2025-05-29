package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityCollectorProductBee;
import net.minecraft.world.entity.player.Player;

public class ContainerCollectorProductBee extends ContainerFullInv<TileEntityCollectorProductBee> {

    public ContainerCollectorProductBee(TileEntityCollectorProductBee tileEntityChickenFarm, Player var1) {
        super(var1, tileEntityChickenFarm, 206);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.fluidSlot, 0, 25, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.outputSlot, 0, 25, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.outputSlot, 3, 122, 99));
        for (int i = 0; i < 16; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.outputSlot1,
                    i, 47 + 18 * (i % 4), 21 + (i / 4) * 18
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.fluidSlot1, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }
    }

}
