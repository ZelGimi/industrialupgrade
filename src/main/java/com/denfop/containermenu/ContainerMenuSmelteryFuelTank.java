package com.denfop.containermenu;

import com.denfop.blockentity.smeltery.BlockEntitySmelteryFuelTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSmelteryFuelTank extends ContainerMenuFullInv<BlockEntitySmelteryFuelTank> {

    public ContainerMenuSmelteryFuelTank(
            BlockEntitySmelteryFuelTank tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
    }

}
