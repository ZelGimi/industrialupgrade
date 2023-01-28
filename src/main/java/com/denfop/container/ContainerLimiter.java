package com.denfop.container;

import com.denfop.gui.HandHeldLimiter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.network.ClientModifiable;
import ic2.core.ContainerFullInv;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerLimiter extends ContainerFullInv<HandHeldLimiter> {

    @ClientModifiable
    private final World world;
    @ClientModifiable
    public double amount;
    @ClientModifiable
    public boolean limit;
    @ClientModifiable
    public boolean update = true;
    private IEnergyTile uut;

    public ContainerLimiter(EntityPlayer player, HandHeldLimiter meter) {
        super(player, meter, 218);
        this.world = player.getEntityWorld();

    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

    }


    public void deleteLimit() {

        IC2.network.get(false).sendContainerFields(this, "amount", "limit", "world");

    }

    public void saveLimit() {

        update = true;
        IC2.network.get(false).sendContainerFields(this, "amount", "limit", "update");

    }


    public void setUut(IEnergyTile uut, double amount, boolean limit) {
        assert this.uut == null;

        this.uut = uut;
        this.amount = amount;
        this.limit = limit;
        this.update = true;
    }

    public void onContainerEvent(String event) {
        super.onContainerEvent(event);


    }


}
