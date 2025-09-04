package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.reactors.water.ISecurity;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWaterSecurity extends ContainerMenuFullInv<BlockEntityInventory> {

    public ContainerMenuWaterSecurity(ISecurity tileEntityPerSecurity, Player var1) {
        super((BlockEntityInventory) tileEntityPerSecurity, var1);
    }

}
