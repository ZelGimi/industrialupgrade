package com.denfop.containermenu;

import com.denfop.blockentity.smeltery.BlockEntitySmelteryFurnace;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSmelteryFurnace extends ContainerMenuFullInv<BlockEntitySmelteryFurnace> {

    public ContainerMenuSmelteryFurnace(
            BlockEntitySmelteryFurnace tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInvSlot(), 0, 77, 34));
    }

}
