package com.denfop.cool;


import com.denfop.api.cooling.ICoolNet;
import com.denfop.api.cooling.ICoolTile;
import com.denfop.componets.CoolComponent;
import ic2.core.block.TileEntityBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class CoolNetGlobal implements ICoolNet {

    private static Map<World, CoolNetLocal> worldToEnergyNetMap;

    static {
        CoolNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static CoolNetGlobal initialize() {
        new EventHandler();
        return new CoolNetGlobal();
    }

    public static void onWorldUnload(World world) {
        final CoolNetLocal local = CoolNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static CoolNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!CoolNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            CoolNetGlobal.worldToEnergyNetMap.put(world, new CoolNetLocal(world));
        }
        return CoolNetGlobal.worldToEnergyNetMap.get(world);
    }

    public static void onTickStart(final World world) {
        final CoolNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickStart();
        }
    }

    public static void onTickEnd(final World world) {
        final CoolNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public ICoolTile getTile(final World var1, final BlockPos var2) {
        if (var1.getTileEntity(var2) instanceof ICoolTile) {
            return (ICoolTile) var1.getTileEntity(var2);
        }
        if (var1.getTileEntity(var2) instanceof TileEntityBlock) {
            TileEntityBlock tile = (TileEntityBlock) var1.getTileEntity(var2);
            assert tile != null;
            if (tile.hasComponent(CoolComponent.class)) {
                return tile.getComponent(CoolComponent.class).getDelegate();
            }
        }
        return null;
    }

    @Override
    public ICoolTile getSubTile(final World var1, final BlockPos var2) {
        return getTile(var1, var2);
    }

    @Override
    public <T extends TileEntity & ICoolTile> void addTile(final T var1) {
        final CoolNetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final ICoolTile var1) {
        final CoolNetLocal local = getForWorld(((TileEntity) var1).getWorld());
        local.removeTile(var1);
    }


}
