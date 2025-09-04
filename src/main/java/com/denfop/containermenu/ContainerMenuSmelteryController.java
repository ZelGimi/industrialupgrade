package com.denfop.containermenu;

import com.denfop.blockentity.smeltery.BlockEntitySmelteryController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSmelteryController extends ContainerMenuFullInv<BlockEntitySmelteryController> {

    public ContainerMenuSmelteryController(
            BlockEntitySmelteryController tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);

    }

}
