package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamSolidFluidMixer;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamSolidFluidMixer extends ContainerFullInv<TileEntitySteamSolidFluidMixer> {

    public ContainerSteamSolidFluidMixer(Player var1, TileEntitySteamSolidFluidMixer tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 60, 44));


    }

}
