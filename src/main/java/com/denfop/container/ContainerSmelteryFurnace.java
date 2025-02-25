package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryFurnace;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSmelteryFurnace extends ContainerFullInv<TileEntitySmelteryFurnace> {

    public ContainerSmelteryFurnace(
            TileEntitySmelteryFurnace tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInvSlot(), 0, 77, 34));
    }

}
