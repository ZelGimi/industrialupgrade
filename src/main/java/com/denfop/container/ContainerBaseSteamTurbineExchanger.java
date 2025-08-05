package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.exchanger.TileEntityBaseSteamTurbineExchanger;
import net.minecraft.world.entity.player.Player;

public class ContainerBaseSteamTurbineExchanger extends ContainerFullInv<TileEntityBaseSteamTurbineExchanger> {

    public ContainerBaseSteamTurbineExchanger(TileEntityBaseSteamTurbineExchanger tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 80, 35));
    }

}
