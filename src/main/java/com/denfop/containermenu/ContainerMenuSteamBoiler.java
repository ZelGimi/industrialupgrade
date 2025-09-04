package com.denfop.containermenu;


import com.denfop.blockentity.mechanism.steam.BlockEntitySteamBoiler;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamBoiler extends ContainerMenuFullInv<BlockEntitySteamBoiler> {

    public ContainerMenuSteamBoiler(Player entityPlayer, BlockEntitySteamBoiler tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
