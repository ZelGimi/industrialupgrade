package com.denfop.container;

import com.denfop.tiles.cyclotron.TileEntityCyclotronChamber;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCyclotronChamber extends ContainerFullInv<TileEntityCyclotronChamber> {

    public ContainerCyclotronChamber(
            TileEntityCyclotronChamber tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInputSlot(), 0, 81, 45));
    }

}
