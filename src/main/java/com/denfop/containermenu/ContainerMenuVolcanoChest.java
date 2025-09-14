package com.denfop.containermenu;


import com.denfop.blockentity.mechanism.BlockEntityVolcanoChest;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuVolcanoChest extends ContainerMenuFullInv<BlockEntityVolcanoChest> {

    public ContainerMenuVolcanoChest(BlockEntityVolcanoChest tileEntityVolcanoChest, Player var1) {
        super(tileEntityVolcanoChest, var1);
        for (int i = 0; i < 27; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityVolcanoChest.invSlot, i, 8 + (i % 9) * 18, 22 + (i / 9) * 18));
        }
    }

}
