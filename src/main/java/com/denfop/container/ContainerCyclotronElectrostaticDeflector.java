package com.denfop.container;

import com.denfop.tiles.cyclotron.TileEntityCyclotronElectrostaticDeflector;
import net.minecraft.world.entity.player.Player;

public class ContainerCyclotronElectrostaticDeflector extends ContainerFullInv<TileEntityCyclotronElectrostaticDeflector> {

    public ContainerCyclotronElectrostaticDeflector(
            TileEntityCyclotronElectrostaticDeflector tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getOutputSlot(), 0, 81, 45));
    }

}
