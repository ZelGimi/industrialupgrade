package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.exchanger.TileEntityBaseSteamTurbineExchanger;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntityExchanger;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBaseSteamTurbineExchanger extends ContainerFullInv<TileEntityBaseSteamTurbineExchanger> {

    public ContainerBaseSteamTurbineExchanger(TileEntityBaseSteamTurbineExchanger tileEntityExchanger, EntityPlayer var1) {
        super(var1, tileEntityExchanger);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 80, 38));
    }

}
