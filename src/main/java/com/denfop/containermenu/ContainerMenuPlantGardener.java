package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPlantGardener;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPlantGardener extends ContainerMenuFullInv<BlockEntityPlantGardener> {

    public ContainerMenuPlantGardener(BlockEntityPlantGardener tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output, i, 10 + i * 18, 18));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
