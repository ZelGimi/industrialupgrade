package com.denfop.api.sytem;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class GlobalNet implements IGlobalNet {

    private final EnergyType type;
    Map<Integer, ILocalNet> worldILocalNetMap = new HashMap<>(3);

    public GlobalNet(EnergyType element) {
        this.type = element;
        EnergyBase.listGlobal.add(this);
        EnergyBase.globalNetMap.put(this.type, this);
    }

    @Override
    public ITile getSubTile(final World var1, final BlockPos var2) {
        ILocalNet localNet = worldILocalNetMap.get(var1.provider.getDimension());
        if (localNet == null) {
            return null;
        } else {
            return localNet.getTileEntity(var2);
        }
    }

    @Override
    public void addTile(final World var1, final ITile var2) {
        ILocalNet localNet = worldILocalNetMap.get(var1.provider.getDimension());
        if (localNet == null) {
            localNet = new LocalNet(this.type);
            localNet.addTile(var2);
            worldILocalNetMap.put(var1.provider.getDimension(), localNet);
        } else {
            localNet.addTile(var2);
        }

    }

    @Override
    public void removeTile(final World var1, final ITile var2) {
        ILocalNet localNet = worldILocalNetMap.get(var1.provider.getDimension());
        if (localNet != null) {
            localNet.removeTile(var2);
        }

    }


    @Override
    public ILocalNet getLocalSystem(final World world) {
        return worldILocalNetMap.get(world.provider.getDimension());
    }

    @Override
    public ILocalNet getLocalSystem(final int id) {
        return worldILocalNetMap.get(id);
    }

    @Override
    public void onUnload(int id) {
        ILocalNet localNet = worldILocalNetMap.get(id);
        if (localNet != null) {
            localNet.onUnload();
        }
    }

    @Override
    public Map<Integer, ILocalNet> getLocalNetMap() {
        return worldILocalNetMap;
    }

    @Override
    public void TickEnd(int id) {
        ILocalNet localNet = worldILocalNetMap.get(id);
        if (localNet != null) {
            localNet.TickEnd();
        }
    }

    @Override
    public NodeStats getNodeStats(final ITile delegate, final World world) {
        ILocalNet localNet = worldILocalNetMap.get(world.provider.getDimension());
        if (localNet != null) {
            return localNet.getNodeStats(delegate);
        }
        return null;
    }

}
