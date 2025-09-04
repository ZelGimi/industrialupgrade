package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityQuarryVein;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuQuarryVein extends ContainerMenuFullInv<BlockEntityQuarryVein> {

    public ContainerMenuQuarryVein(Player entityPlayer, BlockEntityQuarryVein tileEntity1) {
        this(entityPlayer, tileEntity1, 196);

    }

    public ContainerMenuQuarryVein(Player entityPlayer, BlockEntityQuarryVein tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
