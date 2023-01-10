package com.denfop.api.energy;


import com.denfop.Config;
import com.denfop.api.IAdvEnergyNet;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.IEnergyNetEventReceiver;
import ic2.api.energy.NodeStats;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.ILocatable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.lwjgl.Sys;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class EnergyNetGlobal implements IAdvEnergyNet {

    public static IAdvEnergyNet instance;
    private static Map<Integer, EnergyNetLocal> worldToEnergyNetMap;
    private static Map<Integer, List<BlockPos>> worldToEnergyNetList;
    public static long tick = 0;
    static {
        EnergyNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
        EnergyNetGlobal.worldToEnergyNetList = new HashMap<>();
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

    public static void addEnergyTile(final World world,BlockPos blockPos) {

        final int id = world.provider.getDimension();
        List<BlockPos> list = worldToEnergyNetList.get(id);
        if(list == null){
            list = new ArrayList<>();
            list.add(blockPos);
            worldToEnergyNetList.put(id,list);
        }else{
            if(!list.contains(blockPos))
            list.add(blockPos);
        }
    }
    public static void removeEnergyTile(final int id,BlockPos blockPos) {


        List<BlockPos> list = worldToEnergyNetList.get(id);
        if(list != null){
            list.remove(blockPos);
        }
    }
    public static void addEnergyTileFromSave(final int id,BlockPos blockPos) {

       List<BlockPos> list = worldToEnergyNetList.get(id);
        if(list == null){
            list = new ArrayList<>();
            list.add(blockPos);
            worldToEnergyNetList.put(id,list);
        }else{
            if(!list.contains(blockPos))
            list.add(blockPos);
        }
    }
    public static Map<Integer, List<BlockPos>> getWorldToEnergyNetList() {
        return worldToEnergyNetList;
    }

    public static void onTickEnd(final World world) {
        final EnergyNetLocal energyNet = getForWorld(world);
        if(tick == 0) {
            tick = 1;
            for(Map.Entry<Integer, List<BlockPos>> entry : worldToEnergyNetList.entrySet()){
                World world1 = DimensionManager.getWorld(entry.getKey());
                List<BlockPos> blockPos = entry.getValue();
                List<BlockPos> deletePos = new ArrayList<>();
                for(BlockPos pos : blockPos){
                    TileEntity tile = world1.getTileEntity(pos);
                    IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(world1, pos);
                    if(iEnergyTile != null || tile == null) {
                        deletePos.add(pos);
                        continue;
                    }
                    if (tile instanceof cofh.redstoneflux.api.IEnergyHandler) {
                        if (tile instanceof cofh.redstoneflux.api.IEnergyProvider && tile instanceof cofh.redstoneflux.api.IEnergyReceiver) {
                            MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(world1, tile, new EnergyRFSinkSource(tile)));

                        }
                        if (tile instanceof cofh.redstoneflux.api.IEnergyProvider) {
                            MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(world1, tile, new EnergyRFSource(tile)));

                        }
                        if (tile instanceof cofh.redstoneflux.api.IEnergyReceiver)
                            MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(world1, tile, new EnergyRFSink(tile)));
                        continue;
                    }
                    for (EnumFacing facing : EnumFacing.values()) {
                        if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
                            IEnergyStorage energy_storage = tile.getCapability(CapabilityEnergy.ENERGY, facing);
                            if (energy_storage != null) {
                                if (energy_storage.canExtract() && energy_storage.canReceive()) {
                                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(world1, tile,
                                            new EnergyFESinkSource(energy_storage, tile)
                                    ));
                                    break;
                                }
                                if (energy_storage.canExtract()) {
                                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(world1, tile, new EnergyFESource(energy_storage, tile)));
                                    break;
                                }
                                MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(world1, tile, new EnergyFESink(energy_storage, tile)));
                                break;
                            }
                        }
                    }

                }
                blockPos.removeAll(deletePos);
            }
        }
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
