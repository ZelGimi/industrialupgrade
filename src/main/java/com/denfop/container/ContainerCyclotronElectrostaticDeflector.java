package com.denfop.container;

import com.denfop.tiles.cyclotron.TileEntityCyclotronElectrostaticDeflector;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCyclotronElectrostaticDeflector extends ContainerFullInv<TileEntityCyclotronElectrostaticDeflector> {

    public ContainerCyclotronElectrostaticDeflector(
            TileEntityCyclotronElectrostaticDeflector tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getOutputSlot(), 0, 81, 45));
    }

}
