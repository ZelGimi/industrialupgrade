package com.denfop.api.sytem;

import com.denfop.api.energy.NodeStats;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class GlobalNet implements IGlobalNet {

    private final EnergyType type;
    Map<ResourceKey<Level>, ILocalNet> worldILocalNetMap = new HashMap<>(3);

    public GlobalNet(EnergyType element) {
        this.type = element;
        EnergyBase.listGlobal.add(this);
        EnergyBase.globalNetMap.put(this.type, this);
    }

    @Override
    public ITile getSubTile(final Level var1, final BlockPos var2) {
        ILocalNet localNet = worldILocalNetMap.get(var1.dimension());
        if (localNet == null) {
            return null;
        } else {
            return localNet.getTileEntity(var2);
        }
    }

    @Override
    public void addTile(final Level var1, final ITile var2) {
        ILocalNet localNet = worldILocalNetMap.get(var1.dimension());
        if (localNet == null) {
            localNet = new LocalNet(this.type);
            localNet.addTile(var2);
            worldILocalNetMap.put(var1.dimension(), localNet);
        } else {
            localNet.addTile(var2);
        }

    }

    @Override
    public void removeTile(final Level var1, final ITile var2) {
        ILocalNet localNet = worldILocalNetMap.get(var1.dimension());
        if (localNet != null) {
            localNet.removeTile(var2);
        }

    }


    @Override
    public ILocalNet getLocalSystem(final Level world) {
        return worldILocalNetMap.get(world.dimension());
    }

    @Override
    public ILocalNet getLocalSystem(final ResourceKey<Level> id) {
        return worldILocalNetMap.get(id);
    }

    @Override
    public void onUnload(ResourceKey<Level> id) {
        ILocalNet localNet = worldILocalNetMap.get(id);
        if (localNet != null) {
            localNet.onUnload();
        }
    }

    @Override
    public Map<ResourceKey<Level>, ILocalNet> getLocalNetMap() {
        return worldILocalNetMap;
    }

    @Override
    public void TickEnd(ResourceKey<Level> id) {
        ILocalNet localNet = worldILocalNetMap.get(id);
        if (localNet != null) {
            localNet.TickEnd();
        }
    }

    @Override
    public NodeStats getNodeStats(final ITile delegate, final Level world) {
        ILocalNet localNet = worldILocalNetMap.get(world.dimension());
        if (localNet != null) {
            return localNet.getNodeStats(delegate);
        }
        return null;
    }

}
