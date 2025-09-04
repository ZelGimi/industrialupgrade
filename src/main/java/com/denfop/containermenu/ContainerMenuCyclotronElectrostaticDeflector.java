package com.denfop.containermenu;

import com.denfop.blockentity.cyclotron.BlockEntityCyclotronElectrostaticDeflector;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCyclotronElectrostaticDeflector extends ContainerMenuFullInv<BlockEntityCyclotronElectrostaticDeflector> {

    public ContainerMenuCyclotronElectrostaticDeflector(
            BlockEntityCyclotronElectrostaticDeflector tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getOutputSlot(), 0, 81, 45));
    }

}
