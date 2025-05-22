package com.denfop.container;

import com.denfop.tiles.gasturbine.TileEntityGasTurbineRecuperator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasTurbineRecuperator extends ContainerFullInv<TileEntityGasTurbineRecuperator> {

    public ContainerGasTurbineRecuperator(
            TileEntityGasTurbineRecuperator tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getExchanger(), 0, 81, 45));
    }

}
