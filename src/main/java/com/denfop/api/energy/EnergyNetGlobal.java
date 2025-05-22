package com.denfop.api.energy;


import com.denfop.api.IAdvEnergyNet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EnergyNetGlobal implements IAdvEnergyNet {

    public static IAdvEnergyNet instance;
    public static long tick = 0;
    public static IEnergyTile EMPTY = new EnergyTile() {

    };
    private static Map<ResourceKey<Level>, EnergyNetLocal> worldToEnergyNetMap;

    static {
        EnergyNetGlobal.worldToEnergyNetMap = new HashMap<>();
    }

    private boolean transformer;
    private boolean hasrestrictions;
    private boolean explosing;
    private boolean ignoring;
    private boolean losing;

    public EnergyNetGlobal() {
        instance = this;
    }

    public static EnergyNetLocal getForWorld(final Level world) {
        if (world == null) {
            return EnergyNetLocal.EMPTY;
        }
        ResourceKey<Level> id = world.dimension();
        if (!worldToEnergyNetMap.containsKey(id)) {
            final EnergyNetLocal local = new EnergyNetLocal(world);
            worldToEnergyNetMap.put(id, local);
            return local;
        }
        return worldToEnergyNetMap.getOrDefault(id, EnergyNetLocal.EMPTY);
    }

    public static EnergyNetLocal getForWorld(final ResourceKey<Level> id) {
        return worldToEnergyNetMap.getOrDefault(id, EnergyNetLocal.EMPTY);
    }


    public static void onTickEnd(final Level world) {
        final EnergyNetLocal energyNet = getForWorld(world);
        if (energyNet != EnergyNetLocal.EMPTY) {
            energyNet.onTickEnd();
        }
    }


    public static EnergyNetGlobal initialize() {
        new EventHandler();
        instance = new EnergyNetGlobal();
        instance.update();
        return (EnergyNetGlobal) instance;
    }

    public static void onWorldUnload(final Level world) {
        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(world.dimension());
        if (local != EnergyNetLocal.EMPTY) {
            local.onUnload();
            worldToEnergyNetMap.remove(world.dimension());
        }

    }

    public IEnergyTile getTileEntity(final Level world, final int x, final int y, final int z) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileEntity(new BlockPos(x, y, z));
        }
        return EMPTY;
    }

    public IEnergyTile getTileEntity(final Level world, BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileEntity(pos);
        }
        return EMPTY;
    }


    @Override
    public IEnergyTile getTile(final Level world, final BlockPos blockPos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileEntity(blockPos);
        }
        return EMPTY;
    }


    @Override
    public Level getWorld(final IEnergyTile tile) {
        if (tile == null) {
            return null;
        } else if (tile instanceof BlockEntity) {
            return ((BlockEntity) tile).getLevel();
        } else if (tile.getTileEntity() != null) {
            return tile.getTileEntity().getLevel();
        } else {
            throw new UnsupportedOperationException("unlocatable tile type: " + tile.getClass().getName());
        }
    }

    @Override
    public BlockPos getPos(final IEnergyTile iEnergyTile) {
        final EnergyNetLocal local = getForWorld(this.getWorld(iEnergyTile));
        if (local != EnergyNetLocal.EMPTY) {
            return local.getPos(iEnergyTile);
        }
        return null;
    }

    @Override
    public NodeStats getNodeStats(final IEnergyTile te) {
        final EnergyNetLocal local = getForWorld(getWorld(te));
        if (local == EnergyNetLocal.EMPTY) {
            return new NodeStats(0.0, 0.0);
        }
        return local.getNodeStats(te);
    }


    public double getPowerFromTier(final int tier) {


        return tier < 14 ? 8.0D * Math.pow(4.0D, tier) : 9.223372036854776E18D;

    }

    public int getTierFromPower(final double power) {
        if (power <= 0.0) {
            return 0;
        }
        return Math.min(14, (int) Math.ceil(Math.log(power / 8.0) / Math.log(4.0)));
    }


    @Override
    public SunCoef getSunCoefficient(final Level world) {
        return getForWorld(world).getSuncoef();
    }

    @Override
    public boolean getTransformerMode() {
        return this.transformer;
    }

    @Override
    public Map<ChunkPos, List<IEnergySink>> getChunkPosListMap(final Level world) {
        return getForWorld(world).getChunkPosListMap();
    }

    @Override
    public boolean getLosing() {
        return getTransformerMode() && this.losing;
    }

    @Override
    public boolean needExplosion() {
        return getTransformerMode() && this.explosing;
    }

    @Override
    public boolean needIgnoringTiers() {
        return getTransformerMode() && !this.ignoring;
    }

    @Override
    public boolean hasRestrictions() {
        return getTransformerMode() && this.hasrestrictions;
    }

    @Override
    public BlockEntity getBlockPosFromEnergyTile(final IEnergyTile tile) {
        final EnergyNetLocal local = getForWorld(getWorld(tile));
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileFromIEnergy(tile);
        } else {
            return null;
        }

    }

    @Override
    public List<Path> getEnergyPaths(final Level world, final BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        IEnergyTile energyTile = local.getChunkCoordinatesIEnergyTileMap().get(pos);
        if (energyTile != EMPTY) {
            return local.getEnergyPaths(energyTile);
        }
        return new ArrayList<>();
    }

    @Override
    public void update() {
        this.transformer = true;
        this.losing = true;
        this.ignoring = false;
        this.explosing = true;
        this.hasrestrictions = true;
    }

}
