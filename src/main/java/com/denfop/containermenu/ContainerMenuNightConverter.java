package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityNightConverter;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuNightConverter extends ContainerMenuFullInv<BlockEntityNightConverter> {

    public ContainerMenuNightConverter(BlockEntityNightConverter tleEntityMatterFactory, Player var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 70, 17));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 70, 60));
    }

}
