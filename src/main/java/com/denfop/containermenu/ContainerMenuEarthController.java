package com.denfop.containermenu;

import com.denfop.blockentity.quarry_earth.BlockEntityEarthQuarryController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuEarthController extends ContainerMenuFullInv<BlockEntityEarthQuarryController> {

    public ContainerMenuEarthController(BlockEntityEarthQuarryController tileEntityEarthQuarryController, Player entityPlayer) {
        super(tileEntityEarthQuarryController, entityPlayer);
    }

}
