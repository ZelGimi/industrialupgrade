package com.denfop.api.energy;


import com.denfop.Config;
import com.denfop.api.IAdvEnergyNet;
import ic2.api.info.ILocatable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;


public class EnergyNetGlobal implements IAdvEnergyNet {

    private static final List<Integer> integerList = new ArrayList<>();
    public static IAdvEnergyNet instance;
    public static long tick = 0;
    public static IAdvEnergyTile EMPTY = new BasicEnergyTile() {
        @Override
        protected String getNbtTagName() {
            return "BasicTile";
        }
    };
    private static Map<Integer, EnergyNetLocal> worldToEnergyNetMap;
    private static Map<Integer, List<BlockPos>> worldToEnergyNetList;

    static {
        EnergyNetGlobal.worldToEnergyNetMap = new WeakHashMap<>(3);
        EnergyNetGlobal.worldToEnergyNetList = new HashMap<>(3);
    }

    private boolean transformer;
    private boolean hasrestrictions;
    private boolean explosing;
    private boolean ignoring;
    private boolean losing;

    public EnergyNetGlobal() {
        instance = this;
    }

    public static EnergyNetLocal getForWorld(final World world) {
        if (world == null) {
            return EnergyNetLocal.EMPTY;
        }
        final int id = world.provider.getDimension();
        if (!worldToEnergyNetMap.containsKey(id)) {
            final EnergyNetLocal local = new EnergyNetLocal(world);
            worldToEnergyNetMap.put(id, local);
            return local;
        }
        return worldToEnergyNetMap.getOrDefault(id, EnergyNetLocal.EMPTY);
    }

    public static EnergyNetLocal getForWorld(final int id) {
        return worldToEnergyNetMap.getOrDefault(id, EnergyNetLocal.EMPTY);
    }


    public static void addEnergyTileFromSave(final int id, BlockPos blockPos) {

        List<BlockPos> list = worldToEnergyNetList.get(id);
        if (list == null) {
            list = new ArrayList<>();
            list.add(blockPos);
            worldToEnergyNetList.put(id, list);
        } else {
            if (!list.contains(blockPos)) {
                list.add(blockPos);
            }
        }
    }

    public static Map<Integer, List<BlockPos>> getWorldToEnergyNetList() {
        return worldToEnergyNetList;
    }

    public static void onTickEnd(final World world) {
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

    public static void onWorldUnload(final World world) {
        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(world.provider.getDimension());
        if (local != EnergyNetLocal.EMPTY) {
            local.onUnload();
        }
        integerList.remove((Integer) world.provider.getDimension());
    }

    public IAdvEnergyTile getTileEntity(final World world, final int x, final int y, final int z) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileEntity(new BlockPos(x, y, z));
        }
        return EMPTY;
    }

    public IAdvEnergyTile getTileEntity(final World world, BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileEntity(pos);
        }
        return EMPTY;
    }


    @Override
    public IAdvEnergyTile getTile(final World world, final BlockPos blockPos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileEntity(blockPos);
        }
        return EMPTY;
    }

    @Override
    public IAdvEnergyTile getSubTile(final World world, final BlockPos blockPos) {


        return this.getTileEntity(world, blockPos);
    }


    @Override
    public World getWorld(final IAdvEnergyTile tile) {
        if (tile == null) {
            return null;
        } else if (tile instanceof ILocatable) {
            return ((ILocatable) tile).getWorldObj();
        } else if (tile instanceof TileEntity) {
            return ((TileEntity) tile).getWorld();
        } else if (tile instanceof BasicEnergyTile) {
            return ((BasicEnergyTile) tile).getWorldObj();
        } else {
            throw new UnsupportedOperationException("unlocatable tile type: " + tile.getClass().getName());
        }
    }

    @Override
    public BlockPos getPos(final IAdvEnergyTile iEnergyTile) {
        final EnergyNetLocal local = getForWorld(this.getWorld(iEnergyTile));
        if (local != EnergyNetLocal.EMPTY) {
            return local.getPos(iEnergyTile);
        }
        return null;
    }

    @Override
    public NodeStats getNodeStats(final IAdvEnergyTile te) {
        final EnergyNetLocal local = getForWorld(getWorld(te));
        if (local == EnergyNetLocal.EMPTY) {
            return new NodeStats(0.0, 0.0, 0.0);
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
        return (int) Math.ceil(Math.log(power / 8.0) / Math.log(4.0));
    }

    @Override
    public double getRFFromEU(final int amount) {
        return amount * Config.coefficientrf;
    }

    @Override
    public EnergyNetLocal getEnergyLocal(final World world) {
        return getForWorld(world);
    }


    @Override
    public SunCoef getSunCoefficient(final World world) {
        return getForWorld(world).getSuncoef();
    }

    @Override
    public boolean getTransformerMode() {
        return this.transformer;
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
    public TileEntity getBlockPosFromEnergyTile(final IAdvEnergyTile tile) {
        final EnergyNetLocal local = getForWorld(getWorld(tile));
        if (local != EnergyNetLocal.EMPTY) {
            return local.getTileFromIEnergy(tile);
        } else {
            return null;
        }

    }

    @Override
    public List<EnergyNetLocal.EnergyPath> getEnergyPaths(final World world, final BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        IAdvEnergyTile energyTile = local.getChunkCoordinatesIAdvEnergyTileMap().get(pos);
        if (energyTile != EMPTY) {
            return local.getEnergyPaths(energyTile);
        }
        return new ArrayList<>();
    }

    @Override
    public void update() {
        this.transformer = Config.newsystem;
        this.losing = Config.enablelosing;
        this.ignoring = Config.enableIC2EasyMode;
        this.explosing = Config.enableexlposion;
        this.hasrestrictions = !Config.cableEasyMode;
    }

}
