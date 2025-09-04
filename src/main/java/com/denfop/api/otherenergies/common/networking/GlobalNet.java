package com.denfop.api.otherenergies.common.networking;

import com.denfop.api.energy.networking.NodeStats;
import com.denfop.api.otherenergies.common.EnergyBase;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.interfaces.LocalNet;
import com.denfop.api.otherenergies.common.interfaces.Tile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class GlobalNet implements com.denfop.api.otherenergies.common.interfaces.GlobalNet {

    private final EnergyType type;
    Map<ResourceKey<Level>, LocalNet> worldILocalNetMap = new HashMap<>(3);

    public GlobalNet(EnergyType element) {
        this.type = element;
        EnergyBase.listGlobal.add(this);
        EnergyBase.globalNetMap.put(this.type, this);
    }

    @Override
    public Tile getSubTile(final Level var1, final BlockPos var2) {
        LocalNet localNet = worldILocalNetMap.get(var1.dimension());
        if (localNet == null) {
            return null;
        } else {
            return localNet.getTileEntity(var2);
        }
    }

    @Override
    public void addTile(final Level var1, final Tile var2) {
        LocalNet localNet = worldILocalNetMap.get(var1.dimension());
        if (localNet == null) {
            localNet = new com.denfop.api.otherenergies.common.networking.LocalNet(this.type);
            localNet.addTile(var2);
            worldILocalNetMap.put(var1.dimension(), localNet);
        } else {
            localNet.addTile(var2);
        }

    }

    @Override
    public void removeTile(final Level var1, final Tile var2) {
        LocalNet localNet = worldILocalNetMap.get(var1.dimension());
        if (localNet != null) {
            localNet.removeTile(var2);
        }

    }


    @Override
    public LocalNet getLocalSystem(final Level world) {
        return worldILocalNetMap.get(world.dimension());
    }

    @Override
    public LocalNet getLocalSystem(final ResourceKey<Level> id) {
        return worldILocalNetMap.get(id);
    }

    @Override
    public void onUnload(ResourceKey<Level> id) {
        LocalNet localNet = worldILocalNetMap.get(id);
        if (localNet != null) {
            localNet.onUnload();
        }
    }

    @Override
    public Map<ResourceKey<Level>, LocalNet> getLocalNetMap() {
        return worldILocalNetMap;
    }

    @Override
    public void TickEnd(ResourceKey<Level> id) {
        LocalNet localNet = worldILocalNetMap.get(id);
        if (localNet != null) {
            localNet.TickEnd();
        }
    }

    @Override
    public NodeStats getNodeStats(final Tile delegate, final Level world) {
        LocalNet localNet = worldILocalNetMap.get(world.dimension());
        if (localNet != null) {
            return localNet.getNodeStats(delegate);
        }
        return null;
    }

}
