package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamSqueezer;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamSqueezer extends ContainerFullInv<TileEntitySteamSqueezer> {

    public ContainerSteamSqueezer(Player entityPlayer, TileEntitySteamSqueezer tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
