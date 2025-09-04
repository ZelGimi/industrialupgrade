package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityMatterFactory;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuMatterFactory extends ContainerMenuFullInv<BlockEntityMatterFactory> {

    public ContainerMenuMatterFactory(BlockEntityMatterFactory tileEntityMatterFactory, Player var1) {
        super(tileEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityMatterFactory.inputSlotA, 0, 70, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntityMatterFactory.outputSlot, 0, 70, 60));
    }

}
