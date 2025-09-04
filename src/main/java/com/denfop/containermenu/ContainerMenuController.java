package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.energy.BlockEntityEnergyController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuController extends ContainerMenuFullInv<BlockEntityEnergyController> {

    public ContainerMenuController(BlockEntityEnergyController tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1);
    }


}
