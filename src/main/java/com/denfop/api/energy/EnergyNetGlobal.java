package com.denfop.api.energy;


import com.denfop.Config;
import com.denfop.api.IAdvEnergyNet;
import ic2.api.energy.IEnergyNetEventReceiver;
import ic2.api.energy.NodeStats;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.ILocatable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class EnergyNetGlobal implements IAdvEnergyNet {

    public static IAdvEnergyNet instance;
    private static Map<Integer, EnergyNetLocal> worldToEnergyNetMap;

    static {
        EnergyNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    private boolean transformer;
    private boolean hasrestrictions;
    private boolean explosing;
    private boolean ignoring;
    private boolean losing;

    public static EnergyNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        final int id = world.provider.getDimension();
        if (!worldToEnergyNetMap.containsKey(id)) {
            worldToEnergyNetMap.put(id, new EnergyNetLocal(world));
        }
        return worldToEnergyNetMap.get(id);
    }


    public static void onTickEnd(final World world) {
        final EnergyNetLocal energyNet = getForWorld(world);

        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }


    public static EnergyNetGlobal initialize() {
        MinecraftForge.EVENT_BUS.unregister(ic2.core.energy.grid.EventHandler.class);
        new EventHandler();
        instance = new EnergyNetGlobal();
        instance.update();
        return (EnergyNetGlobal) instance;
    }

    public static void onWorldUnload(final World world) {
        final EnergyNetLocal local = EnergyNetGlobal.worldToEnergyNetMap.get(world.provider.getDimension());
        if (local != null) {
            local.onUnload();


        }
    }

    public IEnergyTile getTileEntity(final World world, final int x, final int y, final int z) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != null) {
            return local.getTileEntity(new BlockPos(x, y, z));
        }
        return null;
    }

    public IEnergyTile getTileEntity(final World world, BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != null) {
            return local.getTileEntity(pos);
        }
        return null;
    }


    @Override
    public IEnergyTile getTile(final World world, final BlockPos blockPos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != null) {
            return local.getTileEntity(blockPos);
        }
        return null;
    }

    @Override
    public IEnergyTile getSubTile(final World world, final BlockPos blockPos) {


        return this.getTileEntity(world, blockPos);
    }

    @Override
    public <T extends TileEntity & IEnergyTile> void addTile(final T t) {

    }

    @Override
    public <T extends ILocatable & IEnergyTile> void addTile(final T t) {

    }

    @Override
    public void removeTile(final IEnergyTile iEnergyTile) {

    }

    @Override
    public World getWorld(final IEnergyTile tile) {
        if (tile == null) {
            return null;
        } else if (tile instanceof ILocatable) {
            return ((ILocatable) tile).getWorldObj();
        } else if (tile instanceof TileEntity) {
            return ((TileEntity) tile).getWorld();
        } else {
            throw new UnsupportedOperationException("unlocatable tile type: " + tile.getClass().getName());
        }
    }

    @Override
    public BlockPos getPos(final IEnergyTile iEnergyTile) {
        final EnergyNetLocal local = getForWorld(this.getWorld(iEnergyTile));
        if (local != null) {
            return local.getPos(iEnergyTile);
        }
        return null;
    }

    @Override
    public NodeStats getNodeStats(final IEnergyTile te) {
        final EnergyNetLocal local = getForWorld(getWorld(te));
        if (local == null) {
            return new NodeStats(0.0, 0.0, 0.0);
        }
        return local.getNodeStats(te);
    }

    @Override
    public boolean dumpDebugInfo(
            final World world,
            final BlockPos blockPos,
            final PrintStream printStream,
            final PrintStream printStream1
    ) {
        return false;
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
    public TileEntity getBlockPosFromEnergyTile(final IEnergyTile tile) {
        final EnergyNetLocal local = getForWorld(getWorld(tile));
        try {
            return local.getTileFromIEnergy(tile);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<EnergyNetLocal.EnergyPath> getEnergyPaths(final World world, final BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        IEnergyTile energyTile = local.getChunkCoordinatesIEnergyTileMap().get(pos);
        if (energyTile != null) {
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

    @Override
    public LimitInfo getLimitInfo(final World world, final BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        IEnergyTile energyTile = local.getChunkCoordinatesIEnergyTileMap().get(pos);

        if (energyTile instanceof IEnergySink) {
            final EnergyNetLocal.EnergyPath energyPath = local.getPathFromSink((IEnergySink) energyTile);
            return new LimitInfo((IEnergySink) energyTile, world, energyPath.isLimit, energyPath.limit_amount);
        }
        return null;
    }

    public void deleteLimit(final World world, final IEnergySink energySink) {
        final EnergyNetLocal local = getForWorld(world);
        local.deleteLimit(energySink);
    }

    public void setLimit(final World world, final IEnergySink energySink, double amount) {
        final EnergyNetLocal local = getForWorld(world);
        local.setLimit(energySink, amount);
    }

    public synchronized void registerEventReceiver(IEnergyNetEventReceiver receiver) {

    }

    public synchronized void unregisterEventReceiver(IEnergyNetEventReceiver receiver) {

    }

}
