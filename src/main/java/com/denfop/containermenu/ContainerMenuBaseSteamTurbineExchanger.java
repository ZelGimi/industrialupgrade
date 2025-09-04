package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.exchanger.BlockEntityBaseSteamTurbineExchanger;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBaseSteamTurbineExchanger extends ContainerMenuFullInv<BlockEntityBaseSteamTurbineExchanger> {

    public ContainerMenuBaseSteamTurbineExchanger(BlockEntityBaseSteamTurbineExchanger tileEntityExchanger, Player var1) {
        super(var1, tileEntityExchanger);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 80, 35));
    }

}
