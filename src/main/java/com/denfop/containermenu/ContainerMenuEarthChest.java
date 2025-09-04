package com.denfop.containermenu;

import com.denfop.blockentity.quarry_earth.BlockEntityChest;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuEarthChest extends ContainerMenuFullInv<BlockEntityChest> {

    public ContainerMenuEarthChest(BlockEntityChest tileEntityChest, Player var1) {
        super(tileEntityChest, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChest.getSlot(), i, 10 + i * 18, 30));
        }
    }

}
