package com.denfop.containermenu;

import com.denfop.blockentity.cyclotron.BlockEntityCyclotronController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCyclotronController extends ContainerMenuFullInv<BlockEntityCyclotronController> {

    public ContainerMenuCyclotronController(
            BlockEntityCyclotronController tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);

    }

}
