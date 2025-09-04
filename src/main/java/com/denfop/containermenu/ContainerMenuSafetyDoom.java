package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityReactorSafetyDoom;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSafetyDoom extends ContainerMenuFullInv<BlockEntityReactorSafetyDoom> {

    public ContainerMenuSafetyDoom(BlockEntityReactorSafetyDoom tileEntityReactorSafetyDoom, Player entityPlayer) {
        super(tileEntityReactorSafetyDoom, entityPlayer);
    }

}
