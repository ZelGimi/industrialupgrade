package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryFurnace;
import net.minecraft.world.entity.player.Player;

public class ContainerSmelteryFurnace extends ContainerFullInv<TileEntitySmelteryFurnace> {

    public ContainerSmelteryFurnace(
            TileEntitySmelteryFurnace tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInvSlot(), 0, 77, 34));
    }

}
