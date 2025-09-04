package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityRefrigeratorCoolant;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRefrigerator extends ContainerMenuFullInv<BlockEntityRefrigeratorCoolant> {

    public ContainerMenuRefrigerator(BlockEntityRefrigeratorCoolant tileEntityRefrigeratorCoolant, Player var1) {
        super(tileEntityRefrigeratorCoolant, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityRefrigeratorCoolant.slot, 0, 90, 30));
        this.addSlotToContainer(new SlotInvSlot(tileEntityRefrigeratorCoolant.fluidSlot, 0, 35, 55));
        this.addSlotToContainer(new SlotInvSlot(tileEntityRefrigeratorCoolant.outputSlot, 0, 55, 55));
        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRefrigeratorCoolant.upgradeSlot, i, 152, 10 + i * 18));
        }
    }

}
