package com.denfop.exp;


import com.denfop.api.exp.IEXPNet;
import com.denfop.api.exp.IEXPTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class EXPNetGlobal implements IEXPNet {

    private static Map<World, EXPNetLocal> worldToEnergyNetMap;

    static {
        EXPNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static EXPNetGlobal initialize() {
        new EventHandler();
        return new EXPNetGlobal();
    }

    public static void onWorldUnload(World world) {
        final EXPNetLocal local = EXPNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static EXPNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!EXPNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            EXPNetGlobal.worldToEnergyNetMap.put(world, new EXPNetLocal(world));
        }
        return EXPNetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final EXPNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IEXPTile getTile(final World var1, final BlockPos var2) {
        final EXPNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }

    @Override
    public IEXPTile getSubTile(final World var1, final BlockPos var2) {
        return getTile(var1, var2);
    }

    @Override
    public <T extends TileEntity & IEXPTile> void addTile(final T var1) {
        final EXPNetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final IEXPTile var1) {
        final EXPNetLocal local = getForWorld(((TileEntity) var1).getWorld());
        local.removeTile(var1);
    }


}
