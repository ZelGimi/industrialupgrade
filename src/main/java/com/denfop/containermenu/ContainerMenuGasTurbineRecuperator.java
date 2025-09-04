package com.denfop.containermenu;

import com.denfop.blockentity.gasturbine.BlockEntityGasTurbineRecuperator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGasTurbineRecuperator extends ContainerMenuFullInv<BlockEntityGasTurbineRecuperator> {

    public ContainerMenuGasTurbineRecuperator(
            BlockEntityGasTurbineRecuperator tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getExchanger(), 0, 80, 35));
    }

}
