package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityCactusFarm;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCactusFarm extends ContainerMenuFullInv<BlockEntityCactusFarm> {

    public ContainerMenuCactusFarm(BlockEntityCactusFarm tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, i, 10 + (i % 9) * 18, 18 + 18 * (i / 9)));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
