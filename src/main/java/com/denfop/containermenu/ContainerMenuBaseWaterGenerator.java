package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.water.BlockEntityBaseWaterGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBaseWaterGenerator extends ContainerMenuFullInv<BlockEntityBaseWaterGenerator> {

    public ContainerMenuBaseWaterGenerator(BlockEntityBaseWaterGenerator windGenerator, Player entityPlayer) {
        super(entityPlayer, windGenerator, 208, 242);
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot, 0, 59, 135));
        this.addSlotToContainer(new SlotInvSlot(windGenerator.slot_blades, 0, 28, 135));

    }


}
