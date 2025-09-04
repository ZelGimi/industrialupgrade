package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPlantCollector;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPlantCollector extends ContainerMenuFullInv<BlockEntityPlantCollector> {

    public ContainerMenuPlantCollector(BlockEntityPlantCollector tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 18; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output, i, 10 + (i % 9) * 18, 18 + 18 * (i / 9)));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
