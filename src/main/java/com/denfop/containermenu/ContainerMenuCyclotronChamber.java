package com.denfop.containermenu;

import com.denfop.blockentity.cyclotron.BlockEntityCyclotronChamber;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCyclotronChamber extends ContainerMenuFullInv<BlockEntityCyclotronChamber> {

    public ContainerMenuCyclotronChamber(
            BlockEntityCyclotronChamber tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(tileEntityGeothermalExchanger, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInputSlot(), 0, 81, 45));
    }

}
