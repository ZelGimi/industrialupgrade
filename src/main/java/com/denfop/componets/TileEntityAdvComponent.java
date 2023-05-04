package com.denfop.componets;

import com.denfop.IUCore;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Components;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.network.GrowingBuffer;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class TileEntityAdvComponent extends TileEntityComponent {

    public TileEntityAdvComponent(final TileEntityBlock parent) {
        super(parent);
    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return false;
    }

    public void updateEntityServer() {
    }

    public void updateEntityClient() {
    }

    public void onBlockActivated() {
    }

    protected void setNetworkUpdate(EntityPlayerMP player, GrowingBuffer data) {
        IUCore.network.get(true).sendComponentUpdate(this.parent, this.toString(), player, data);
    }

    @Override
    public String toString() {
        return Components.getId(this.getClass());
    }

}
