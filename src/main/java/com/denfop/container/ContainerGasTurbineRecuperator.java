package com.denfop.container;

import com.denfop.tiles.gasturbine.TileEntityGasTurbineRecuperator;
import net.minecraft.world.entity.player.Player;

public class ContainerGasTurbineRecuperator extends ContainerFullInv<TileEntityGasTurbineRecuperator> {

    public ContainerGasTurbineRecuperator(
            TileEntityGasTurbineRecuperator tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getExchanger(), 0, 80, 35));
    }

}
