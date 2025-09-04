package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityTransformer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuTransformer extends ContainerMenuFullInv<BlockEntityTransformer> {

    public ContainerMenuTransformer(Player player, BlockEntityTransformer tileEntity1, int height) {
        super(player, tileEntity1, height);
    }


}
