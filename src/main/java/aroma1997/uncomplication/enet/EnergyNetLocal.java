package aroma1997.uncomplication.enet;

import com.denfop.Config;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.heat.IWirelessSource;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.items.armour.ItemArmorAdvHazmat;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import com.denfop.tiles.mechanism.TileEntityCable;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IEnergyWirelessSource;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.energy.tile.IMultiEnergySource;
import ic2.api.info.ILocatable;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.wiring.TileEntityTransformer;
import ic2.core.item.armor.ItemArmorHazmat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class EnergyNetLocal {
    public static double minConductionLoss;
    private static EnumFacing[] directions;
    public static EnergyTransferList list;
    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final Map<EntityLivingBase, Double> entityLivingToShockEnergyMap;
    private final Map<ChunkCoordinates, IEnergyTile> registeredTiles;
    private final Map<ChunkCoordinates, IEnergySource> sources;
    private final WaitingList waitingList;
    private final Map<Chunk, List<IEnergyTile>> registeredTilesInChunk;
    private final Map<Chunk, List<IEnergyWirelessSource>> registeredWirelessSourcesInChunk;
    private final Map<ChunkCoordinates, Chunk> getChunkFromCoord;
    EnergyNetLocal(final World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.entityLivingToShockEnergyMap = new HashMap<>();
        this.registeredTiles = new HashMap<>();
        this.sources = new HashMap<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.registeredTilesInChunk = new HashMap<>();
        this.registeredWirelessSourcesInChunk=new HashMap<>();
        this.getChunkFromCoord= new HashMap<>();
    }
    public List<IEnergyTile> getListEnergyInChunk(Chunk chunk) {
        return registeredTilesInChunk.containsKey(chunk) ? registeredTilesInChunk.get(chunk) : new ArrayList<>() ;
    }
    public void addTile(IEnergyTile tile1) {
        if (tile1 instanceof IMetaDelegate) {
            final List<IEnergyTile> tiles = ((IMetaDelegate) tile1).getSubTiles();
            for (final IEnergyTile tile : tiles) {
                this.addTileEntity(coords(getTileFromIEnergy(tile)), tile1);
            }
            this.addTileEntity(coords(getTileFromIEnergy(tile1)), tile1);

            if (tile1 instanceof IEnergySource) {
                this.sources.put(coords(getTileFromIEnergy(tile1)), (IEnergySource) tile1);
            }
        } else {
            this.addTileEntity(coords(getTileFromIEnergy(tile1)), tile1);
        }
    }

    public void addTileEntity(final ChunkCoordinates coords, final IEnergyTile tile) {
        if (this.registeredTiles.containsKey(coords)) {
            return;
        }
        TileEntity te =  getTileFromIEnergy(tile);
        this.registeredTiles.put(coords, tile);
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if(tile instanceof IEnergySink ) {
            if (this.registeredTilesInChunk.containsKey(chunk)) {
                List<IEnergyTile> lst = this.registeredTilesInChunk.get(chunk);
                lst.add(tile);
                if(registeredWirelessSourcesInChunk.containsKey(chunk)){
                    List<IEnergyWirelessSource> lst1 = this.registeredWirelessSourcesInChunk.get(chunk);
                    for(IEnergyWirelessSource wirelessSource : lst1)
                        wirelessSource.setList(lst);

                }
            } else {
                List<IEnergyTile> l = new ArrayList<>();
                l.add(tile);
                this.registeredTilesInChunk.put(chunk, l);

            }
            this.getChunkFromCoord.put(coords,chunk);
        }else{
            if(tile instanceof IEnergyWirelessSource){
                if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                    List<IEnergyWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                    lst.add((IEnergyWirelessSource) tile);
                    ((IEnergyWirelessSource) tile).setList(getListEnergyInChunk(((IEnergyWirelessSource) tile).getChunk()));
                } else {
                    List<IEnergyWirelessSource> l = new ArrayList<>();
                    l.add((IEnergyWirelessSource) tile);
                    this.registeredWirelessSourcesInChunk.put(chunk, l);
                }
            }
        }
        this.update(coords.x, coords.y, coords.z);
        if (tile instanceof IEnergyAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof IEnergySource && !(tile instanceof IMetaDelegate)) {
            this.sources.put(coords, (IEnergySource) tile);
        }
    }

    public void removeTile(IEnergyTile tile1) {
        if (tile1 instanceof IMetaDelegate) {
            final List<IEnergyTile> tiles = ((IMetaDelegate) tile1).getSubTiles();
            this.removeTileEntity(coords(getTileFromIEnergy(tile1)), tile1);
            for (final IEnergyTile tile : tiles) {
                final ChunkCoordinates coord = coords(getTileFromIEnergy(tile1));
                this.removeTileEntity(coords(getTileFromIEnergy(tile1)), tile);
                final Chunk chunk = this.getChunkFromCoord.get(coord);
                if(tile instanceof IEnergySink )
                    if( this.registeredTilesInChunk.containsKey(chunk)) {
                        List<IEnergyTile> lst = this.registeredTilesInChunk.get(chunk);
                        lst.remove(tile);
                        this.getChunkFromCoord.remove(coord,chunk);
                    }
                if(tile instanceof IEnergyWirelessSource){
                    if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                        List<IEnergyWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                        lst.remove((IEnergyWirelessSource) tile);
                    }
                }
            }

        } else {
            ChunkCoordinates coord=  coords(getTileFromIEnergy(tile1));
            if(tile1 instanceof IEnergySink || tile1 instanceof IEnergyWirelessSource) {
                final Chunk chunk = this.getChunkFromCoord.get(coord);
                if (tile1 instanceof IEnergySink)
                    if (this.registeredTilesInChunk.containsKey(chunk)) {
                        List<IEnergyTile> lst = this.registeredTilesInChunk.get(chunk);
                        lst.remove(tile1);
                        this.getChunkFromCoord.remove(coord,chunk);
                    }
                if (tile1 instanceof IEnergyWirelessSource) {
                    if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                        List<IEnergyWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                        lst.remove((IEnergyWirelessSource) tile1);
                    }
                }
            }
            this.removeTileEntity(coord, tile1);
        }
    }

    public void removeTileEntity(final ChunkCoordinates coords, IEnergyTile tile) {
        if (!this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.remove(coords);
        this.update(coords.x, coords.y, coords.z);
        if (tile instanceof IEnergyAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IEnergyAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof IEnergySource) {
            this.sources.remove(coords);
            this.energySourceToEnergyPathMap.remove((IEnergySource)tile);
        }
    }

    public double emitEnergyFrom(final ChunkCoordinates coords, final IEnergySource energySource, double amount) {
        if (!this.registeredTiles.containsKey(coords)) {
            return amount;
        }
        if (!this.energySourceToEnergyPathMap.containsKey(energySource)) {
            final EnergyPathMap energySourceToEnergyPathMap = this.energySourceToEnergyPathMap;
            energySourceToEnergyPathMap.put(energySource, this.discover(energySource, EnergyTransferList.getMaxEnergy(energySource, energySource.getOfferedEnergy())));
        }
        List<EnergyPath> activeEnergyPaths = new Vector<>();
        double totalInvLoss = 0.0;
        final double source = energySource.getSourceTier();
        for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.get(energySource)) {
            assert energyPath.target instanceof IEnergySink;
            final IEnergySink energySink = (IEnergySink) energyPath.target;
            if (energySink.getDemandedEnergy() <= 0.0) {
                continue;
            }
            if (energyPath.loss >= amount) {
                continue;
            }
            if (Config.enableIC2EasyMode && this.conductorToWeak(energyPath.conductors, amount)) {
                continue;
            }
            totalInvLoss += 1.0 / energyPath.loss;
            activeEnergyPaths.add(energyPath);
        }
        Collections.shuffle(activeEnergyPaths);
        for (double i = activeEnergyPaths.size() - amount; i > 0; --i) {
            final EnergyPath removedEnergyPath = activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
            totalInvLoss -= 1.0 / removedEnergyPath.loss;
        }
        final Map<EnergyPath, Double> suppliedEnergyPaths = new HashMap<>();
        while (!activeEnergyPaths.isEmpty() && amount > 0) {
            double energyConsumed = 0;
            double newTotalInvLoss = 0.0;
            final List<EnergyPath> currentActiveEnergyPaths = activeEnergyPaths;
            activeEnergyPaths = new Vector<>();
            for (final EnergyPath energyPath2 : currentActiveEnergyPaths) {
                final IEnergySink energySink2 = (IEnergySink) energyPath2.target;
                final double energyProvided = Math.floor(Math.round(amount / totalInvLoss / energyPath2.loss));
                final double energyLoss = Math.floor(energyPath2.loss);
                if (energyProvided > energyLoss) {
                    final double providing = energyProvided - energyLoss;

                    double adding = Math.min(providing, energySink2.getDemandedEnergy());
                    if (adding <= 0.0 && EnergyTransferList.hasOverrideInput(energySink2)) {
                        adding = EnergyTransferList.getOverrideInput(energySink2);
                    }
                    if (adding <= 0.0) {
                        continue;
                    }
                    final int tier = energySink2.getSinkTier();
                    final int tier1 =      EnergyNet.instance.getTierFromPower(providing);

                    if (tier1 > tier && !Config.enableIC2EasyMode) {
                        this.explodeTiles(energySink2);
                    } else {
                        double energyReturned = energySink2.injectEnergy(energyPath2.targetDirection, adding, source);
                        if (energyReturned == 0.0) {
                            activeEnergyPaths.add(energyPath2);
                            newTotalInvLoss += 1.0 / energyPath2.loss;
                        } else if (energyReturned >= energyProvided - energyLoss) {
                            energyReturned = energyProvided - energyLoss;
                        }
                        energyConsumed += (adding - energyReturned + energyLoss);
                        final double energyInjected = (adding - energyReturned);
                        if (!suppliedEnergyPaths.containsKey(energyPath2)) {
                            suppliedEnergyPaths.put(energyPath2, energyInjected);
                        } else {
                            suppliedEnergyPaths.put(energyPath2, energyInjected + suppliedEnergyPaths.get(energyPath2));
                        }
                    }
                } else {
                    activeEnergyPaths.add(energyPath2);
                    newTotalInvLoss += 1.0 / energyPath2.loss;
                }
            }
            if (energyConsumed == 0 && !activeEnergyPaths.isEmpty()) {
                final EnergyPath removedEnergyPath2 = activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
                newTotalInvLoss -= 1.0 / removedEnergyPath2.loss;
            }
            totalInvLoss = newTotalInvLoss;
            amount -= energyConsumed;
        }
        for (final Map.Entry<EnergyPath, Double> entry : suppliedEnergyPaths.entrySet()) {
            final EnergyPath energyPath3 = entry.getKey();
            final double energyInjected2 = entry.getValue();
            final EnergyPath energyPath5;
            final EnergyPath energyPath4 = energyPath5 = energyPath3;
            energyPath5.totalEnergyConducted += energyInjected2;
            energyPath4.maxSendedEnergy = (long) Math.max(energyPath4.maxSendedEnergy, energyInjected2);
            if (energyInjected2 > energyPath3.minInsulationEnergyAbsorption) {
                final List<EntityLivingBase> entitiesNearEnergyPath =
                        this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                                new AxisAlignedBB(energyPath3.minX - 1, energyPath3.minY - 1, energyPath3.minZ - 1,
                                        energyPath3.maxX + 2, energyPath3.maxY + 2, energyPath3.maxZ + 2));
                for (final EntityLivingBase entityLiving : entitiesNearEnergyPath) {
                    double maxShockEnergy = 0;
                    for (final IEnergyConductor energyConductor : energyPath3.conductors) {
                        final TileEntity te = (TileEntity) energyConductor;
                        if (entityLiving.boundingBox.intersects(new AxisAlignedBB(te.getPos().getX() - 1, te.getPos().getY() - 1,
                                te.getPos().getZ() - 1,
                                te.getPos().getX() + 2, te.getPos().getY() + 2, te.getPos().getZ() + 2))) {
                            if (te instanceof TileEntityCable)
                                continue;

                            final double shockEnergy = (energyInjected2 - energyConductor.getInsulationEnergyAbsorption());
                            if (shockEnergy > maxShockEnergy) {
                                maxShockEnergy = shockEnergy;
                            }

                            if (energyConductor.getInsulationEnergyAbsorption() == energyPath3.minInsulationEnergyAbsorption) {
                                break;
                            }
                        }
                    }

                    if (this.entityLivingToShockEnergyMap.containsKey(entityLiving)) {
                        this.entityLivingToShockEnergyMap.put(entityLiving, this.entityLivingToShockEnergyMap.get(entityLiving) + maxShockEnergy);
                    } else {
                        this.entityLivingToShockEnergyMap.put(entityLiving, maxShockEnergy);
                    }
                }
                if (energyInjected2 >= energyPath3.minInsulationBreakdownEnergy) {
                    for (final IEnergyConductor energyConductor2 : energyPath3.conductors) {
                        if (energyInjected2 >= energyConductor2.getInsulationBreakdownEnergy()) {
                            energyConductor2.removeInsulation();
                            if (energyConductor2.getInsulationEnergyAbsorption() >= energyPath3.minInsulationEnergyAbsorption) {
                                continue;
                            }
                            energyPath3.minInsulationEnergyAbsorption = (int) energyConductor2.getInsulationEnergyAbsorption();
                        }
                    }
                }
            }
            if (energyInjected2 >= energyPath3.minConductorBreakdownEnergy) {
                for (final IEnergyConductor energyConductor3 : energyPath3.conductors) {
                    if (energyInjected2 >= energyConductor3.getConductorBreakdownEnergy() && !Config.enableIC2EasyMode) {
                        energyConductor3.removeConductor();
                    }
                }
            }
        }
        return amount;
    }

    public double getTotalEnergyEmitted(final IEnergyTile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IEnergyConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(tileEntity)) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        if (tileEntity instanceof IEnergySource && this.energySourceToEnergyPathMap.containsKey((IEnergySource)tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((IEnergySource)tileEntity)) {
                ret += energyPath2.totalEnergyConducted;
            }
        }
        return ret;
    }

    public double getTotalEnergySunken(final IEnergyTile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IEnergyConductor || tileEntity instanceof IEnergySink) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                if ((tileEntity instanceof IEnergySink && energyPath.target == tileEntity) || (tileEntity instanceof IEnergyConductor && energyPath.conductors.contains(tileEntity))) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        return ret;
    }
  public TileEntity  getTileFromIEnergy(IEnergyTile tile){
      TileEntity te = null;
      if(tile instanceof TileEntity)
          te = (TileEntity) tile;
      if(tile instanceof ILocatable){
          te = this.world.getTileEntity (((ILocatable)tile).getPosition());
      }
      if(tile instanceof BasicSink){
          te = this.world.getTileEntity (((BasicSink)tile).getPosition());
      }
      return  te;
  }
    private List<EnergyPath> discover(final IEnergyTile emitter, final double lossLimit) {
        final Map<IEnergyTile, EnergyBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<IEnergyTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.remove();
            TileEntity tile = getTileFromIEnergy(emitter);
            if (!tile.isInvalid()) {
                double currentLoss = 0.0;
                if (this.registeredTiles.get(coords(tile)) != null && this.registeredTiles.get(coords(tile)) != emitter && reachedTileEntities.containsKey(currentTileEntity)) {
                    currentLoss = reachedTileEntities.get(currentTileEntity).loss;
                }
                final List<EnergyTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
                for (final EnergyTarget validReceiver : validReceivers) {
                    if (validReceiver.tileEntity != emitter) {
                        double additionalLoss = 0.0;
                        if (validReceiver.tileEntity instanceof IEnergyConductor) {
                            additionalLoss = ((IEnergyConductor) validReceiver.tileEntity).getConductionLoss();
                            if (additionalLoss < 1.0E-4) {
                                additionalLoss = 1.0E-4;
                            }
                            if (currentLoss + additionalLoss >= lossLimit) {
                                continue;
                            }
                        }
                        if (reachedTileEntities.containsKey(validReceiver.tileEntity) && reachedTileEntities.get(validReceiver.tileEntity).loss <= currentLoss + additionalLoss) {
                            continue;
                        }
                        reachedTileEntities.put(validReceiver.tileEntity, new EnergyBlockLink(validReceiver.direction,
                                currentLoss + additionalLoss));
                        if (!(validReceiver.tileEntity instanceof IEnergyConductor)) {
                            continue;
                        }
                        tileEntitiesToCheck.remove(validReceiver.tileEntity);
                        tileEntitiesToCheck.add(validReceiver.tileEntity);
                    }
                }
            }
        }
        final List<EnergyPath> energyPaths = new LinkedList<>();
        for (final Map.Entry<IEnergyTile, EnergyBlockLink> entry : reachedTileEntities.entrySet()) {
            IEnergyTile tileEntity = entry.getKey();
            if ((tileEntity instanceof IEnergySink)) {
                EnergyBlockLink energyBlockLink = entry.getValue();
                final EnergyPath energyPath = new EnergyPath();
                energyPath.loss = Math.max(energyBlockLink.loss, 0.1);
                energyPath.target = tileEntity;
                energyPath.targetDirection = energyBlockLink.direction;
                if (emitter instanceof IEnergySource) {
                    while (true) {
                        TileEntity te = getTileFromIEnergy(tileEntity);
                        tileEntity = this.getTileEntity(te.getPos().offset( energyBlockLink.direction));
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof IEnergyConductor)) {
                            break;
                        }
                        final IEnergyConductor energyConductor = (IEnergyConductor) tileEntity;
                        if (te.getPos().getX() < energyPath.minX) {
                            energyPath.minX = te.getPos().getX();
                        }
                        if (te.getPos().getY() < energyPath.minY) {
                            energyPath.minY = te.getPos().getY();
                        }
                        if (te.getPos().getZ() < energyPath.minZ) {
                            energyPath.minZ = te.getPos().getZ();
                        }
                        if (te.getPos().getX() > energyPath.maxX) {
                            energyPath.maxX = te.getPos().getX();
                        }
                        if (te.getPos().getY()  > energyPath.maxY) {
                            energyPath.maxY = te.getPos().getY();
                        }
                        if (te.getPos().getZ() > energyPath.maxZ) {
                            energyPath.maxZ = te.getPos().getZ();
                        }
                        energyPath.conductors.add(energyConductor);
                        if (energyConductor.getInsulationEnergyAbsorption() < energyPath.minInsulationEnergyAbsorption) {
                            energyPath.minInsulationEnergyAbsorption = (int) energyConductor.getInsulationEnergyAbsorption();
                        }
                        if (energyConductor.getInsulationBreakdownEnergy() < energyPath.minInsulationBreakdownEnergy) {
                            energyPath.minInsulationBreakdownEnergy = (int) energyConductor.getInsulationBreakdownEnergy();
                        }
                        if (energyConductor.getConductorBreakdownEnergy() < energyPath.minConductorBreakdownEnergy) {
                            energyPath.minConductorBreakdownEnergy = (int) energyConductor.getConductorBreakdownEnergy();
                        }
                        energyBlockLink = reachedTileEntities.get(tileEntity);
                        if (energyBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to " +
                                "incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile " +
                                "entities below)\nE: " + emitter + " (" + te.getPos().getX() + "," + te.getPos().getY()  + "," + te.getPos().getZ()  + ")\n" + "C: " + tileEntity + " (" + te.getPos().getX() + "," + te.getPos().getY() + "," + te.getPos().getZ() + ")\n" + "R: " + energyPath.target + " (" +getTileFromIEnergy(energyPath.target).getPos().getX() + "," + getTileFromIEnergy(energyPath.target).getPos().getY() + "," +getTileFromIEnergy( energyPath.target).getPos().getZ() + ")");
                    }
                }
                energyPaths.add(energyPath);
            }
        }
        return energyPaths;
    }

    private boolean conductorToWeak(final Set<IEnergyConductor> par1, final double energyToSend) {
        boolean flag = false;
        for (final IEnergyConductor cond : par1) {
            if (cond.getConductorBreakdownEnergy() <= energyToSend) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    public IEnergyTile getNeighbor(final IEnergyTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        TileEntity te = null;
        if(tile instanceof TileEntity)
            te = (TileEntity) tile;
        if(tile instanceof ILocatable){
            te = this.world.getTileEntity (((ILocatable)tile).getPosition());
        }
        if(tile instanceof BasicSink){
            te = this.world.getTileEntity (((BasicSink)tile).getPosition());
        }
        return this.getTileEntity(te.getPos().offset(dir));
    }

    private List<EnergyTarget> getValidReceivers(final IEnergyTile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();
        for (final EnumFacing direction : EnergyNetLocal.directions) {
            if (emitter instanceof IMetaDelegate) {
                final IMetaDelegate meta = (IMetaDelegate) emitter;
                final List<IEnergyTile> targets = meta.getSubTiles();
                for (final IEnergyTile tile : targets) {
                    final IEnergyTile target = getNeighbor(tile, direction);
                    final TileEntity te1 = getTileFromIEnergy(target);
                    if (target == emitter) {
                        continue;
                    }
                    if (target == null || !this.registeredTiles.containsKey(coords(te1))) {
                        continue;
                    }
                    final EnumFacing inverseDirection = direction.getOpposite();
                    if (reverse) {
                        if (!(emitter instanceof IEnergyAcceptor) || !(target instanceof IEnergyEmitter)) {
                            continue;
                        }
                        final IEnergyEmitter sender = (IEnergyEmitter) target;
                        final IEnergyAcceptor receiver = (IEnergyAcceptor) emitter;
                        if (!sender.emitsEnergyTo(receiver, inverseDirection) || !receiver.acceptsEnergyFrom(sender,
                                direction)) {
                            continue;
                        }
                    } else {
                        if (!(emitter instanceof IEnergyEmitter) || !(target instanceof IEnergyAcceptor)) {
                            continue;
                        }
                        final IEnergyEmitter sender = (IEnergyEmitter) emitter;
                        final IEnergyAcceptor receiver = (IEnergyAcceptor) target;
                        if (!sender.emitsEnergyTo(receiver, direction) || !receiver.acceptsEnergyFrom(sender,
                                inverseDirection)) {
                            continue;
                        }
                    }
                    validReceivers.add(new EnergyTarget(target, inverseDirection));
                }
            } else {
                final IEnergyTile target2 = getNeighbor(emitter, direction);
                TileEntity te1 = getTileFromIEnergy(target2);
                if (target2 != null && this.registeredTiles.containsKey(coords(te1))) {
                    final EnumFacing inverseDirection2 = direction.getOpposite();
                    if (reverse) {
                        if (emitter instanceof IEnergyAcceptor && target2 instanceof IEnergyEmitter) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) target2;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) emitter;
                            if (sender2.emitsEnergyTo(receiver2, inverseDirection2) && receiver2.acceptsEnergyFrom(sender2,
                                    direction)) {
                                validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                            }
                        }
                    } else if (emitter instanceof IEnergyEmitter && target2 instanceof IEnergyAcceptor) {
                        final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                        final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
                        if (sender2.emitsEnergyTo(receiver2, direction) && receiver2.acceptsEnergyFrom(sender2, inverseDirection2)) {
                            validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                        }
                    }
                }
            }
        }
        return validReceivers;
    }

    public List<IEnergySource> discoverFirstPathOrSources(final IEnergyTile par1) {
        final Set<IEnergyTile> reached = new HashSet<>();
        final List<IEnergySource> result = new ArrayList<>();
        final List<IEnergyTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IEnergyTile tile = workList.remove(0);
            final TileEntity te = getTileFromIEnergy(tile);
            if (!te.isInvalid()) {
                final List<EnergyTarget> targets = this.getValidReceivers(tile, true);
                for (EnergyTarget energyTarget : targets) {
                    final IEnergyTile target = energyTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IEnergySource) {
                                result.add((IEnergySource) target);
                            } else if (target instanceof IEnergyConductor) {
                                workList.add(target);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ChunkCoordinates coords(final TileEntity par1) {
        if (par1 == null) {
            return null;
        }
        return new ChunkCoordinates(par1.getPos());
    }

    public void onTickStart() {

        if (!Config.damagecable)
            return;
        for (Map.Entry<EntityLivingBase, Double> entry : this.entityLivingToShockEnergyMap.entrySet()) {
            EntityLivingBase target = entry.getKey();
            if (target.isEntityAlive()) {
                if (!(target instanceof EntityPlayer))
                    target.attackEntityFrom(IUDamageSource.current, 1.0F);
                else {
                    EntityPlayer player = (EntityPlayer) target;
                    if (!ItemArmorImprovemedQuantum.hasCompleteHazmat(player)  && !ItemArmorHazmat.hasCompleteHazmat(player) && !ItemArmorAdvHazmat.hasCompleteHazmat(player)) {
                        if (entry.getValue() != 0)
                            player.attackEntityFrom(IUDamageSource.current, 1.0F);
                    }
                }
            }
        }
        this.entityLivingToShockEnergyMap.clear();
    }

    public void onTickEnd() {
        if (this.waitingList.hasWork()) {
            final List<IEnergyTile> tiles = this.waitingList.getPathTiles();
            for (final IEnergyTile tile : tiles) {
                final List<IEnergySource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.energySourceToEnergyPathMap.removeAll(sources);
                }
            }
            this.waitingList.clear();
        }
        for (Map.Entry<ChunkCoordinates, IEnergySource> entry : new HashMap<>(this.sources).entrySet()) {
            if (entry != null) {
                final IEnergySource source = entry.getValue();
                if (source != null) {
                    if (this.energySourceToEnergyPathMap.containsKey(source)) {
                        for (final EnergyPath path : this.energySourceToEnergyPathMap.get(source)) {
                            path.totalEnergyConducted = 0L;
                            path.maxSendedEnergy = 0L;
                        }
                    }

                    double offer =Math.min(source.getOfferedEnergy(),
                            EnergyNet.instance.getPowerFromTier( source.getSourceTier()));
                    if (offer > 0) {
                        for (double packetAmount = this.getPacketAmount(source), i = 0; i < packetAmount; ++i) {
                            offer =Math.min(source.getOfferedEnergy(),
                                    EnergyNet.instance.getPowerFromTier( source.getSourceTier()));
                            if (offer < 1) {
                                break;
                            }
                            final double removed = offer - this.emitEnergyFrom(entry.getKey(), source, offer);
                            if (removed <= 0) {
                                break;
                            }

                            source.drawEnergy(removed);
                        }
                    }
                }
            }
        }
    }

    private double getPacketAmount(final IEnergySource source) {
        if (source instanceof IMultiEnergySource && ((IMultiEnergySource) source).sendMultipleEnergyPackets()) {
            return ((IMultiEnergySource) source).getMultipleEnergyPacketAmount();
        }
        if (source instanceof TileEntityTransformer) {
            TileEntityTransformer.Mode  red =   ReflectionHelper.getPrivateValue(TileEntityTransformer.class,(TileEntityTransformer)source,
                    "transformMode");
            return red ==   TileEntityTransformer.Mode.stepdown  ? 1 : 4;
        }
        if (source instanceof com.denfop.tiles.base.TileEntityTransformer)
            return ((com.denfop.tiles.base.TileEntityTransformer)source).isStepUp()  ? 1 : 4;

        return 1;
    }





    public void explodeTiles(final IEnergySink sink) {
        this.removeTile(sink);
        if (sink instanceof IMetaDelegate) {
            final IMetaDelegate meta = (IMetaDelegate) sink;
            for (final IEnergyTile tile : meta.getSubTiles()) {
                this.explodeMachineAt((getTileFromIEnergy(tile)).getPos(), getTileFromIEnergy(tile));
            }
        } else {
            this.explodeMachineAt((getTileFromIEnergy(sink)).getPos(),
                    getTileFromIEnergy(sink));
        }
    }

    public IEnergyTile getTileEntity(final int x, final int y, final int z) {
        final ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
        if (this.registeredTiles.containsKey(coords)) {
            return this.registeredTiles.get(coords);
        }
        return null;
    }
    public IEnergyTile getTileEntity(BlockPos pos) {
        final ChunkCoordinates coords = new ChunkCoordinates(pos);
        if (this.registeredTiles.containsKey(coords)) {
            return this.registeredTiles.get(coords);
        }
        return null;
    }
    public NodeStats getNodeStats(final IEnergyTile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeStats(received, emitted, EnergyNet.instance.getTierFromPower(this.getVoltage(tile)));
    }

    private double getVoltage(final IEnergyTile tileEntity) {
        double voltage = 0.0;
        if (tileEntity instanceof IEnergySource && this.energySourceToEnergyPathMap.containsKey((IEnergySource)tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((IEnergySource)tileEntity)) {
                voltage = Math.max(voltage, (double) energyPath2.maxSendedEnergy);
            }
        }
        if (tileEntity instanceof IEnergyConductor || tileEntity instanceof IEnergySink) {
            for (final EnergyPath energyPath3 : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                if ((tileEntity instanceof IEnergySink && energyPath3.target == tileEntity) || (tileEntity instanceof IEnergyConductor && energyPath3.conductors.contains(tileEntity))) {
                    voltage = Math.max(voltage, (double) energyPath3.maxSendedEnergy);
                }
            }
        }
        return voltage;
    }
    void explodeMachineAt(BlockPos pos, final TileEntity tile) {

        if (Config.enableexlposion) {

            this.world.setBlockToAir(pos);

            float power = 1;

            final ExplosionIC2 explosion = new ExplosionIC2(this.world, null, 0.5 + pos.getX(), 0.5 + pos.getY(), 0.5 + pos.getZ(),
                    power,
                    0.75f);
            explosion.doExplosion();
        }
    }

    void update(final int x, final int y, final int z) {
        for (final EnumFacing dir : EnumFacing.values()) {
            if (this.world.isChunkGeneratedAt(x + dir.getFrontOffsetX() >> 4, z + dir.getFrontOffsetZ()  >> 4)) {
                BlockPos pos = new BlockPos(x,y ,
                        z).offset(dir);
                this.world.neighborChanged(pos , Blocks.AIR, pos);

            }
        }
    }

    public void onUnload() {
        this.energySourceToEnergyPathMap.clear();
        this.registeredTiles.clear();
        this.sources.clear();
        this.entityLivingToShockEnergyMap.clear();
        this.waitingList.clear();
        this.registeredTilesInChunk.clear();
        this.registeredWirelessSourcesInChunk.clear();
        this.getChunkFromCoord.clear();
    }

    static {
        EnergyNetLocal.minConductionLoss = 1.0E-4;
        EnergyNetLocal.directions = EnumFacing.values();
    }



    static class EnergyTarget {
        final IEnergyTile tileEntity;
        final EnumFacing direction;

        EnergyTarget(final IEnergyTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }
    }

    static class EnergyBlockLink {
        final EnumFacing direction;
        final double loss;

        EnergyBlockLink(final EnumFacing direction, final double loss) {
            this.direction = direction;
            this.loss = loss;
        }
    }

    static class EnergyPath {
        IEnergyTile target;
        EnumFacing targetDirection;
        final Set<IEnergyConductor> conductors;
        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;
        double loss;
        double minInsulationEnergyAbsorption;
        double minInsulationBreakdownEnergy;
        double minConductorBreakdownEnergy;
        long totalEnergyConducted;
        long maxSendedEnergy;

        EnergyPath() {
            this.target = null;
            this.conductors = new HashSet<>();
            this.minX = Integer.MAX_VALUE;
            this.minY = Integer.MAX_VALUE;
            this.minZ = Integer.MAX_VALUE;
            this.maxX = Integer.MIN_VALUE;
            this.maxY = Integer.MIN_VALUE;
            this.maxZ = Integer.MIN_VALUE;
            this.loss = 0.0;
            this.minInsulationEnergyAbsorption = Integer.MAX_VALUE;
            this.minInsulationBreakdownEnergy = Integer.MAX_VALUE;
            this.minConductorBreakdownEnergy = Integer.MAX_VALUE;
            this.totalEnergyConducted = 0L;
            this.maxSendedEnergy = 0L;
        }
    }

    static class EnergyPathMap {
        final Map<IEnergySource, List<EnergyPath>> senderPath;
        final Map<EnergyPath, IEnergySource> pathToSender;

        EnergyPathMap() {
            this.senderPath = new HashMap<>();
            this.pathToSender = new HashMap<>();
        }

        public void put(final IEnergySource par1, final List<EnergyPath> par2) {
            this.senderPath.put(par1, par2);
            for (EnergyPath energyPath : par2) {
                this.pathToSender.put(energyPath, par1);
            }
        }

        public boolean containsKey(final IEnergySource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<EnergyPath> get(final IEnergySource par1) {
            return this.senderPath.get(par1);
        }

        public void remove(final IEnergySource par1) {
            final List<EnergyPath> paths = this.senderPath.remove(par1);
            if (paths != null) {
                for (EnergyPath path : paths) {
                    this.pathToSender.remove(path);
                }
            }
        }

        public void removeAll(final List<IEnergySource> par1) {
            for (IEnergySource iEnergySource : par1) {
                this.remove(iEnergySource);
            }
        }

        public List<EnergyPath> getPaths(final IEnergyAcceptor par1) {
            final List<EnergyPath> paths = new ArrayList<>();
            for (final IEnergySource source : this.getSources(par1)) {
                if (this.containsKey(source)) {
                    paths.addAll(this.get(source));
                }
            }
            return paths;
        }

        public List<IEnergySource> getSources(final IEnergyAcceptor par1) {
            final List<IEnergySource> source = new ArrayList<>();
            for (final Map.Entry<EnergyPath, IEnergySource> entry : this.pathToSender.entrySet()) {
                if (source.contains(entry.getValue())) {
                    continue;
                }
                final EnergyPath path = entry.getKey();
                if ((!(par1 instanceof IEnergyConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IEnergySink) || path.target != par1)) {
                    continue;
                }
                source.add(entry.getValue());
            }
            return source;
        }

        public void clear() {
            this.senderPath.clear();
            this.pathToSender.clear();
        }
    }

    class WaitingList {
        final List<PathLogic> paths;

        WaitingList() {
            this.paths = new ArrayList<>();
        }

        public void onTileEntityAdded(final List<EnergyTarget> around, final IEnergyTile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IEnergyConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final EnergyTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IEnergyConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IEnergyConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IEnergyTile toMove : logic2.tiles) {
                        if (!newLogic.contains(toMove)) {
                            newLogic.add(toMove);
                        }
                    }
                    logic2.clear();
                }
                this.paths.add(newLogic);
            }
            if (!found) {
                this.createNewPath(tile);
            }
        }

        public void onTileEntityRemoved(final IEnergyTile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<IEnergyTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IEnergyTile tile : toRecalculate) {
                this.onTileEntityAdded(EnergyNetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final IEnergyTile par1) {
            final PathLogic logic = new PathLogic();
            logic.add(par1);
            this.paths.add(logic);
        }

        public void clear() {
            if (this.paths.isEmpty()) {
                return;
            }
            for (PathLogic path : this.paths) {
                path.clear();
            }
            this.paths.clear();
        }

        public boolean hasWork() {
            return this.paths.size() > 0;
        }

        public List<IEnergyTile> getPathTiles() {
            final List<IEnergyTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IEnergyTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }
    }

    static class PathLogic {
        final List<IEnergyTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IEnergyTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IEnergyTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IEnergyTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IEnergyTile getRepresentingTile() {
            if (this.tiles.isEmpty()) {
                return null;
            }
            return this.tiles.get(0);
        }
    }
}
