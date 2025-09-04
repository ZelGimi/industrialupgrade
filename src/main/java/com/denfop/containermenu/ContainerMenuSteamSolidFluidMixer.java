package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamSolidFluidMixer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamSolidFluidMixer extends ContainerMenuFullInv<BlockEntitySteamSolidFluidMixer> {

    public ContainerMenuSteamSolidFluidMixer(Player var1, BlockEntitySteamSolidFluidMixer tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 60, 44));


    }

}
