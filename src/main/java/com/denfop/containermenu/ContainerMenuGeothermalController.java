package com.denfop.containermenu;

import com.denfop.blockentity.geothermalpump.BlockEntityGeothermalController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGeothermalController extends ContainerMenuFullInv<BlockEntityGeothermalController> {

    public ContainerMenuGeothermalController(
            BlockEntityGeothermalController tileEntityGeothermalController,
            Player entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
