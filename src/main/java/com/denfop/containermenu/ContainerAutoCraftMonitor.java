package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityAutoCraftMonitor;
import net.minecraft.world.entity.player.Player;

public class ContainerAutoCraftMonitor extends ContainerMenuFullInv<BlockEntityAutoCraftMonitor> {

    public ContainerAutoCraftMonitor(BlockEntityAutoCraftMonitor tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 214, 218, false);
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int width, int height) {
    }


}
