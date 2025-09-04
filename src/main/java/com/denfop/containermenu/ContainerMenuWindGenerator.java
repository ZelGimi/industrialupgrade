package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.wind.BlockEntityWindGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWindGenerator extends ContainerMenuFullInv<BlockEntityWindGenerator> {

    public ContainerMenuWindGenerator(BlockEntityWindGenerator windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 208, 242);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 59, 135));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 28, 135));

    }


}
