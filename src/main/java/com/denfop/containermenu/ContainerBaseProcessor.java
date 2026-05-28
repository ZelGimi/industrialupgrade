package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityBaseProcessor;
import net.minecraft.world.entity.player.Player;

public class ContainerBaseProcessor extends ContainerMenuFullInv<BlockEntityBaseProcessor> {

    public ContainerBaseProcessor(BlockEntityBaseProcessor tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 214, 218, false);
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int width, int height) {


    }


}
