package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityGenomeExtractor;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGenomeExtractor extends ContainerMenuFullInv<BlockEntityGenomeExtractor> {

    public ContainerMenuGenomeExtractor(BlockEntityGenomeExtractor tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.input, 0, 19, 16));
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 19, 34));

    }

}
